package com.roger.data.data_source.weather

import com.roger.data.dto.weather.WeatherDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET(endpoint)
    fun getWeather(
        @Query("Authorization") authorization: String?,
        @Query("locationName") locationName: String?,
        @Query("sort") sort: String?,
    ): Single<WeatherDto>

    companion object {
        private const val endpoint = "/api/v1/rest/datastore/F-C0032-001"
    }
}