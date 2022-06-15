package com.roger.data.network

import retrofit2.Retrofit
import javax.inject.Inject

class WeatherNetworkManager @Inject constructor(retrofit: Retrofit) : NetworkManager(retrofit)