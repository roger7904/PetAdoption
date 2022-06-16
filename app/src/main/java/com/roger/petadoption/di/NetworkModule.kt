package com.roger.petadoption.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.roger.data.BuildConfig
import com.roger.data.network.NetworkManager
import com.roger.data.network.SimpleCallAdapterFactory
import com.roger.data.network.WeatherNetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideStethoInterceptor(): StethoInterceptor {
        return StethoInterceptor()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
        return interceptor
    }

    @GovAuth
    @Singleton
    @Provides
    fun provideHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        stethoInterceptor: StethoInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(stethoInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @WeatherAuth
    @Singleton
    @Provides
    fun provideWeatherHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        stethoInterceptor: StethoInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(stethoInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory {
        return RxJava3CallAdapterFactory.create()
    }

    @GovAuth
    @Singleton
    @Provides
    fun provideRetrofit(
        @GovAuth httpClient: OkHttpClient,
        gson: Gson,
        simpleCallAdapterFactory: SimpleCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient)
            .addCallAdapterFactory(simpleCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @WeatherAuth
    @Singleton
    @Provides
    fun provideWeatherRetrofit(
        @WeatherAuth httpClient: OkHttpClient,
        gson: Gson,
        simpleCallAdapterFactory: SimpleCallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_WEATHER)
            .client(httpClient)
            .addCallAdapterFactory(simpleCallAdapterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideNetworkManager(@GovAuth retrofit: Retrofit): NetworkManager {
        return NetworkManager(retrofit)
    }

    @Singleton
    @Provides
    fun provideWeatherNetworkManager(@WeatherAuth retrofit: Retrofit): WeatherNetworkManager {
        return WeatherNetworkManager(retrofit)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GovAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherAuth
