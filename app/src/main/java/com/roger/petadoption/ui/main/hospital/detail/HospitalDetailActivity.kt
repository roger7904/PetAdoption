package com.roger.petadoption.ui.main.hospital.detail

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityHospitalDetailBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.utils.BitmapHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.io.IOException


@AndroidEntryPoint
class HospitalDetailActivity : BaseActivity<ActivityHospitalDetailBinding>(), OnMapReadyCallback {

    private val viewModel: HospitalDetailViewModel by viewModels()
    private var map: GoogleMap? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationPermissionGranted = false
    private var markers: List<Marker>? = null
    private val markerIcon: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(
            this,
            R.mipmap.ic_marker
        )
    }

    override fun initParam(data: Bundle) {
    }

    override fun getViewModel(): BaseViewModel = viewModel

    override fun initViewBinding(): ActivityHospitalDetailBinding =
        ActivityHospitalDetailBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this@HospitalDetailActivity)

            val mapFragment = supportFragmentManager.findFragmentById(
                R.id.map_fragment
            ) as? SupportMapFragment
            mapFragment?.getMapAsync(this@HospitalDetailActivity)

            viewModel.hospitalInfo.observe(this@HospitalDetailActivity) {
                addMarkers(it)
            }

            btnSearch.setOnClickListener {
                searchArea()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map?.setInfoWindowAdapter(HospitalMarkerInfoWindowAdapter(this@HospitalDetailActivity))

        getLocationPermission()

        updateLocationUISetting()

        //getDeviceLocation()
    }

    private fun addMarkers(hospitalList: List<HospitalEntity>) {
        map?.run {
            markers = hospitalList.mapNotNull { result ->
                val latLng = getLocationFromAddress(result.location)
                addMarker(
                    MarkerOptions()
                        .title(result.name)
                        .icon(markerIcon)
                        .position(latLng ?: return)
                ).apply {
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM)
                    )
                    this?.tag = result
                }
            }
        }
    }

    private fun addMarkersWithLatLng(hospitalList: List<HospitalLocationEntity>?) {
        map?.run {
            markers?.forEach { it.remove() }
            markers = null
            markers = hospitalList?.mapNotNull { result ->
                addMarker(
                    MarkerOptions()
                        .title(result.name)
                        .position(LatLng(result.lat ?: return, result.lng ?: return))
                        .icon(markerIcon)
                ).apply {
                    this?.tag = result
                }
            }
        }
    }

    private fun getLocationFromAddress(strAddress: String?): LatLng? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: LatLng? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)
            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = LatLng(location.latitude, location.longitude)
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return p1
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            viewModel.getHospitalInfo()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    viewModel.getHospitalInfo()
                    //getDeviceLocation()
                }
            }
        }
        updateLocationUISetting()
    }

    private fun updateLocationUISetting() {
        map?.apply {
            isMyLocationEnabled = locationPermissionGranted
            uiSettings.isMapToolbarEnabled = locationPermissionGranted
            uiSettings.isMyLocationButtonEnabled = locationPermissionGranted
        }
    }

    private fun searchArea() {
        val visibleRegion = map?.projection?.visibleRegion?.latLngBounds ?: return
        val startLat = visibleRegion.southwest.latitude
        val endLat = visibleRegion.northeast.latitude
        val startLng = minOf(visibleRegion.northeast.longitude, visibleRegion.southwest.longitude)
        val endLng = maxOf(visibleRegion.northeast.longitude, visibleRegion.southwest.longitude)
        addMarkersWithLatLng(
            viewModel.hospitalLocationList.value?.filter {
                it.lat ?: 0.0 in startLat..endLat && it.lng ?: 0.0 in startLng..endLng
            }
        )
    }

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient?.lastLocation
                locationResult?.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    task.result.latitude, task.result.longitude
                                ), DEFAULT_ZOOM
                            )
                        )
                    } else {
                        Timber.d("Current location is null. Using defaults.")
                        Timber.e(task.exception, "Exception: %s")
                        map?.moveCamera(CameraUpdateFactory
                            .newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM))
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Timber.e(e, e.message)
        }
    }

    companion object {
        private val DEFAULT_LOCATION = LatLng(25.0530895, 121.6047654)
        private const val DEFAULT_ZOOM = 12f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        const val ARG_HOSPITAL_ID = "HOSPITAL_ID"
        const val ARG_HOSPITAL_LIST = "HOSPITAL_LIST"
    }
}