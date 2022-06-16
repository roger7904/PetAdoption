package com.roger.petadoption.ui.main.home.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
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
import com.google.android.gms.maps.model.LatLng
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityPetMapBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.utils.BitmapHelper
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.utils.toPx
import java.lang.Exception
import com.google.android.gms.maps.model.LatLngBounds

@AndroidEntryPoint
class ShelterMapActivity : BaseActivity<ActivityPetMapBinding>(),
    OnMapReadyCallback {

    private val viewModel: ShelterMapViewModel by viewModels()
    private var map: GoogleMap? = null
    private var locationPermissionGranted = false
    private var lastKnownLocation: LatLng = DEFAULT_LOCATION
    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }
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

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map?.setInfoWindowAdapter(ShelterMarkerInfoWindowAdapter(this@ShelterMapActivity))

        map?.setOnInfoWindowClickListener { marker ->
            infoWindowClickEvent(marker)
        }

        getLocationPermission()

        updateLocationUISetting()

        getDeviceLocation()
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
                    map?.animateCamera(
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

    private fun infoWindowClickEvent(marker: Marker) {
        val departureLatLng = lastKnownLocation
        val destinationLatLng = marker.position

        val path: MutableList<LatLng> = ArrayList()

        val context = GeoApiContext.Builder()
            .apiKey(BuildConfig.GOOGLE_MAPS_API_KEY)
            .build()

        val req: DirectionsApiRequest =
            DirectionsApi.getDirections(
                context,
                "${departureLatLng.latitude},${departureLatLng.longitude}",
                "${destinationLatLng.latitude},${destinationLatLng.longitude}"
            )
        try {
            val res: DirectionsResult = req.await()

            //Loop through legs and steps to get encoded polylines of each step
            if (!res.routes.isNullOrEmpty()) {
                val route: DirectionsRoute = res.routes[0]
                if (route.legs != null) {
                    for (i in route.legs.indices) {
                        val leg: DirectionsLeg = route.legs[i]
                        if (leg.steps != null) {
                            for (j in leg.steps.indices) {
                                val step: DirectionsStep = leg.steps[j]
                                if (!step.steps.isNullOrEmpty()) {
                                    for (element in step.steps) {
                                        val step1: DirectionsStep = element
                                        val points1: EncodedPolyline = step1.polyline
                                        //Decode polyline and add points to list of route coordinates
                                        val coords1: List<com.google.maps.model.LatLng> =
                                            points1.decodePath()
                                        for (coord1 in coords1) {
                                            path.add(LatLng(coord1.lat, coord1.lng))
                                        }
                                    }
                                } else {
                                    val points: EncodedPolyline = step.polyline
                                    //Decode polyline and add points to list of route coordinates
                                    val coords: MutableList<com.google.maps.model.LatLng>? =
                                        points.decodePath()
                                    if (coords != null) {
                                        for (coord in coords) {
                                            path.add(LatLng(coord.lat, coord.lng))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(
                this, getString(R.string.shelter_map_route_exception), Toast.LENGTH_SHORT
            ).show()
        }

        //Draw the polyline
        if (path.size > 0) {
            val opts = PolylineOptions()
                .addAll(path)
                .color(ContextCompat.getColor(this, R.color.colorPrimary))
                .width(5.toPx(this))
            map?.addPolyline(opts)
        }

        val builder = LatLngBounds.Builder()
        builder.include(departureLatLng)
        builder.include(destinationLatLng)

        map?.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                builder.build(),
                200)
        )

        marker.hideInfoWindow()
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

    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = LatLng(task.result.latitude, task.result.longitude)
                    }
                }
            }
        } catch (e: SecurityException) {
            Toast.makeText(
                this, getString(R.string.shelter_map_device_location_exception), Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        private val DEFAULT_LOCATION = LatLng(25.0530895, 121.6047654)
        private const val DEFAULT_ZOOM = 12f
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
        const val ARG_PET_MAP_ID = "ARG_PET_MAP_ID"
    }
}