package com.roger.petadoption.di

import com.roger.data.data_source.hospital.HospitalApiService
import com.roger.data.data_source.pet.PetApiService
import com.roger.data.network.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Singleton
    @Provides
    fun providePetApiService(networkManager: NetworkManager): PetApiService {
        return networkManager.getApiService(PetApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideHospitalApiService(networkManager: NetworkManager): HospitalApiService {
        return networkManager.getApiService(HospitalApiService::class.java)
    }
}