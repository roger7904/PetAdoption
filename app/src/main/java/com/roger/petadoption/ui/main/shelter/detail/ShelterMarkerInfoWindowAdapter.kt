package com.roger.petadoption.ui.main.shelter.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ShelterMarkerInfoContentsBinding
import com.roger.petadoption.ui.main.home.detail.WeatherType

class ShelterMarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val shelterEntity = marker.tag as? ShelterEntity ?: return null
        val view =
            LayoutInflater.from(context).inflate(R.layout.shelter_marker_info_contents, null)
        val binding = ShelterMarkerInfoContentsBinding.bind(view)

        binding.tvShelterName.text = shelterEntity.shelterName
        if (shelterEntity.phone.isNullOrEmpty()) binding.tvShelterMobile.visibility = View.GONE
        binding.tvShelterMobile.text = shelterEntity.phone
        binding.tvShelterLocation.text = shelterEntity.address
        binding.ivWeather.setImageDrawable(
            if (shelterEntity.weatherWx == null)
                AppCompatResources.getDrawable(
                    context, R.drawable.ic_google_maps
                )
            else AppCompatResources.getDrawable(
                context, WeatherType.getEnum(shelterEntity.weatherWx ?: "1").iconResId
            )
        )
        if (shelterEntity.weatherMin == null || shelterEntity.weatherMax == null) {
            binding.tvWeather.visibility = View.GONE
        }
        binding.tvWeather.text = context.getString(
            R.string.shelter_map_weather, shelterEntity.weatherMin, shelterEntity.weatherMax
        )
        return view
    }
}