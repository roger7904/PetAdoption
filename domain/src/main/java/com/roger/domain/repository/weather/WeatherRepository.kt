package com.roger.domain.repository.weather

import com.roger.domain.entity.weather.WeatherEntity
import com.roger.domain.use_case.weather.GetWeatherUseCase
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getWeather(param: GetWeatherUseCase.Param): Single<WeatherEntity>
}