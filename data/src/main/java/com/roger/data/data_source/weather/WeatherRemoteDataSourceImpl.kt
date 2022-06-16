package com.roger.data.data_source.weather

import com.roger.data.dto.weather.WeatherDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(private val weatherApiService: WeatherApiService) :
    WeatherDataSource.Remote {

    override fun getWeather(
        authorization: String?,
        locationName: String?,
        sort: String?,
    ): Single<WeatherDto> {
        return weatherApiService.getWeather(
            authorization = authorization,
            locationName = locationName,
            sort = sort
        )
    }
}