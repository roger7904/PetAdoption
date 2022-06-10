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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(
            R.id.map_fragment
        ) as? SupportMapFragment
        mapFragment?.getMapAsync(this@HospitalDetailActivity)

        viewModel.hospitalInfo.observe(this) {
            addMarkers(it)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        getLocationPermission()

        updateLocationUISetting()

        //getDeviceLocation()
    }

    private fun addMarkers(hospitalList: List<HospitalEntity>) {
        map?.run {
            hospitalList.forEach { result ->
                val latLng = getLocationFromAddress(result.location)
                val marker = this.addMarker(
                    MarkerOptions()
                        .title(result.name)
                        .position(latLng ?: return@forEach)
                        .icon(markerIcon)
                )
                map?.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM)
                )
                // Set place as the tag on the marker object so it can be referenced within
                marker?.tag = result
                this.setInfoWindowAdapter(HospitalMarkerInfoWindowAdapter(this@HospitalDetailActivity))
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
    }
}