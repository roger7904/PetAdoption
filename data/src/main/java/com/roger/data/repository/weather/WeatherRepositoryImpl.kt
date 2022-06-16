package com.roger.data.repository.weather

import com.roger.data.data_source.weather.WeatherDataSource
import com.roger.data.mapper.weather.WeatherMapper
import com.roger.domain.entity.weather.WeatherEntity
import com.roger.domain.repository.weather.WeatherRepository
import com.roger.domain.use_case.weather.GetWeatherUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource.Remote,
    private val weatherMapper: WeatherMapper,
) : WeatherRepository {

    override fun getWeather(param: GetWeatherUseCase.Param): Single<WeatherEntity> {
        return weatherDataSource.getWeather(
            param.authorization,
            param.locationName,
            param.sort
        ).map {
            weatherMapper.toEntity(it)
        }
    }
}