package com.roger.petadoption.ui.main.hospital.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HospitalLocationEntity(
    val city: String? = null,
    val number: String? = null,
    val kind: String? = null,
    val status: String? = null,
    val name: String? = null,
    val doctor: String? = null,
    val mobile: String? = null,
    val date: String? = null,
    val location: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
) : Parcelable