package com.roger.domain.use_case.weather

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.weather.WeatherEntity
import com.roger.domain.repository.weather.WeatherRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<GetWeatherUseCase.Param, WeatherEntity>(errorHandler) {

    override fun buildUseCase(param: Param): Single<WeatherEntity> {
        return weatherRepository.getWeather(param)
    }

    data class Param(
        val authorization: String?,
        val locationName: String?,
        val sort: String?,
    )
}