package com.roger.data.network

import retrofit2.Retrofit

class NetworkManager constructor(private val retrofit: Retrofit) {

    fun getRetrofit(): Retrofit {
        return retrofit
    }

    inline fun <reified T> getApiService(serviceClazz: Class<T>): T {
        return getRetrofit().create(serviceClazz)
    }
}