package com.roger.data.data_source.weather

import com.roger.data.dto.weather.WeatherDto
import io.reactivex.rxjava3.core.Single

interface WeatherDataSource {
    interface Remote {
        fun getWeather(
            authorization: String?,
            locationName: String?,
            sort: String?,
        ): Single<WeatherDto>
    }
}