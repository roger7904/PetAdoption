package com.roger.petadoption.ui.main.home.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.roger.domain.entity.pet.PetEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.ShelterMarkerInfoContentsBinding

class ShelterMarkerInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {
    override fun getInfoContents(marker: Marker): View? {
        return null
    }

    override fun getInfoWindow(marker: Marker): View? {
        val petEntity = marker.tag as? PetEntity ?: return null
        val view =
            LayoutInflater.from(context).inflate(R.layout.shelter_marker_info_contents, null)
        val binding = ShelterMarkerInfoContentsBinding.bind(view)

        binding.tvShelterName.text = petEntity.petPlace
        if (petEntity.shelterTel.isNullOrEmpty()) binding.tvShelterMobile.visibility = View.GONE
        binding.tvShelterMobile.text = petEntity.shelterTel
        binding.tvShelterLocation.text = petEntity.shelterAddress
        petEntity.weatherWx?.let {
            binding.ivWeather.setImageDrawable(
                AppCompatResources.getDrawable(
                    context, WeatherType.getEnum(it).iconResId
                )
            )
        }
        binding.tvWeather.text = context.getString(
            R.string.shelter_map_weather, petEntity.weatherMin, petEntity.weatherMax
        )
        return view
    }
}