package com.roger.petadoption.ui.main.hospital.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.petadoption.R
import com.roger.petadoption.databinding.HospitalMarkerInfoContentsBinding

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

        return view
    }
}