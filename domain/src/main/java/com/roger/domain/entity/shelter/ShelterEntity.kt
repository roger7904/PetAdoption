package com.roger.domain.entity.shelter

data class ShelterEntity(
    val id: String? = null,
    val shelterName: String? = null,
    val cityName: String? = null,
    val address: String? = null,
    val phone: String? = null,
    val openTime: String? = null,
    val url: String? = null,
    val lon: String? = null,
    val lat: String? = null,
    val weatherMin: String? = null, // 最低溫度
    val weatherMax: String? = null, // 最高溫度
    val weatherWx: String? = null, // 天氣現象
)