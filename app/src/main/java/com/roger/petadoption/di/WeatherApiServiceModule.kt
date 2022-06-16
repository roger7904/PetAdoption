package com.roger.petadoption.di

import com.roger.data.data_source.weather.WeatherApiService
import com.roger.data.network.WeatherNetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WeatherApiServiceModule {

    @Singleton
    @Provides
    fun providePetApiService(weatherNetworkManager: WeatherNetworkManager): WeatherApiService {
        return weatherNetworkManager.getApiService(WeatherApiService::class.java)
    }
}