package com.roger.petadoption.ui.main.shelter.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
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
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.model.*
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ActivityShelterDetailBinding
import com.roger.petadoption.ui.base.BaseActivity
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.utils.BitmapHelper
import com.roger.petadoption.utils.toPx
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class ShelterDetailActivity : BaseActivity<ActivityShelterDetailBinding>(), OnMapReadyCallback {

    private val viewModel: ShelterDetailViewModel by viewModels()
    private var map: GoogleMap? = null
    private var locationPermissionGranted = false
    private var markers: List<Marker>? = null
    private var polyline: Polyline? = null
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

    override fun initViewBinding(): ActivityShelterDetailBinding =
        ActivityShelterDetailBinding.inflate(layoutInflater)

    override fun initView(savedInstanceState: Bundle?) {
        binding?.run {
            val mapFragment = supportFragmentManager.findFragmentById(
                R.id.map_fragment
            ) as? SupportMapFragment
            mapFragment?.getMapAsync(this@ShelterDetailActivity)

            viewModel.shelterInfo.observe(this@ShelterDetailActivity) {
                addMarkers(it)
            }

            btnSearch.setOnClickListener {
                searchArea()
            }
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map?.setInfoWindowAdapter(ShelterMarkerInfoWindowAdapter(this@ShelterDetailActivity))

        map?.setOnInfoWindowClickListener { marker ->
            infoWindowClickEvent(marker)
        }

        getLocationPermission()

        updateLocationUISetting()

        getDeviceLocation()
    }

    private fun addMarkers(shelterEntity: ShelterEntity) {
        map?.run {
            markers = listOf(shelterEntity.let { result ->
                val latLng =
                    LatLng(result.lat?.toDouble() ?: return, result.lon?.toDouble() ?: return)
                addMarker(
                    MarkerOptions()
                        .title(result.shelterName)
                        .icon(markerIcon)
                        .position(latLng)
                ).apply {
                    map?.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM)
                    )
                    this?.tag = result
                }
            } ?: return)
        }
    }

    private fun addMarkersWithLatLng(shelterList: List<ShelterEntity>?) {
        map?.run {
            markers?.forEach { it.remove() }
            markers = null
            markers = shelterList?.mapNotNull { result ->
                addMarker(
                    MarkerOptions()
                        .title(result.shelterName)
                        .position(LatLng(result.lat?.toDouble() ?: return,
                            result.lon?.toDouble() ?: return))
                        .icon(markerIcon)
                ).apply {
                    this?.tag = result
                }
            }
        }
    }

    private fun infoWindowClickEvent(marker: Marker) {
        polyline?.remove()
        polyline = null
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
            polyline = map?.addPolyline(opts)
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
            viewModel.getShelterInfo()
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
                    viewModel.getShelterInfo()
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
            viewModel.shelterList.value?.filter {
                it.lat?.toDouble() ?: 0.0 in startLat..endLat && it.lon?.toDouble() ?: 0.0 in startLng..endLng
            }
        )
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
        const val ARG_SHELTER_ID = "SHELTER_ID"
    }
}