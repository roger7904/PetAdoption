package com.roger.domain.entity.hospital

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class HospitalEntity(
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
    val weatherMin: String? = null, // 最低溫度
    val weatherMax: String? = null, // 最高溫度
    val weatherWx: String? = null, // 天氣現象
) : Parcelable