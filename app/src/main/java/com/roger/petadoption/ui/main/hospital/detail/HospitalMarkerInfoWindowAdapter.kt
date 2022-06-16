package com.roger.petadoption.ui.main.hospital.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.HospitalMarkerInfoContentsBinding
import com.roger.petadoption.ui.main.home.detail.WeatherType

class HospitalMarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val hospitalEntity = marker.tag as? HospitalEntity ?: return null
        val view =
            LayoutInflater.from(context).inflate(R.layout.hospital_marker_info_contents, null)
        val binding = HospitalMarkerInfoContentsBinding.bind(view)

        binding.tvHospitalName.text = hospitalEntity.name
        if (hospitalEntity.mobile.isNullOrEmpty()) binding.tvHospitalMobile.visibility = View.GONE
        binding.tvHospitalMobile.text = hospitalEntity.mobile
        binding.tvHospitalLocation.text = hospitalEntity.location
        binding.ivWeather.setImageDrawable(
            if (hospitalEntity.weatherWx == null)
                AppCompatResources.getDrawable(
                    context, R.drawable.ic_google_maps
                )
            else AppCompatResources.getDrawable(
                context, WeatherType.getEnum(hospitalEntity.weatherWx ?: "1").iconResId
            )
        )
        if (hospitalEntity.weatherMin == null || hospitalEntity.weatherMax == null) {
            binding.tvWeather.visibility = View.GONE
        }
        binding.tvWeather.text = context.getString(
            R.string.shelter_map_weather, hospitalEntity.weatherMin, hospitalEntity.weatherMax
        )

        return view
    }
}