package com.roger.petadoption.ui.main.home.detail

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityPetMapBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.utils.BitmapHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class ShelterMapActivity : BaseActivity<ActivityPetMapBinding>(), OnMapReadyCallback {

    private val viewModel: PetMapViewModel by viewModels()
    private var map: GoogleMap? = null
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

    override fun initViewBinding(): ActivityPetMapBinding =
        ActivityPetMapBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            val mapFragment = supportFragmentManager.findFragmentById(
                R.id.map_fragment
            ) as? SupportMapFragment
            mapFragment?.getMapAsync(this@ShelterMapActivity)

            viewModel.petInfo.observe(this@ShelterMapActivity) {
                addMarkers(it)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map?.setInfoWindowAdapter(ShelterMarkerInfoWindowAdapter(this@ShelterMapActivity))

        getLocationPermission()

        updateLocationUISetting()
    }

    private fun addMarkers(petEntity: PetEntity) {
        map?.run {
            petEntity.let { result ->
                val latLng = getLocationFromAddress(result.shelterAddress)
                addMarker(
                    MarkerOptions()
                        .title(result.petPlace)
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
            viewModel.getPetInfo()
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
                    viewModel.getPetInfo()
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

    companion object {
        private const val DEFAULT_ZOOM = 12f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        const val ARG_PET_MAP_ID = "ARG_PET_MAP_ID"
    }
}