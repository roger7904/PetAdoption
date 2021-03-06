package com.roger.petadoption.di

import com.roger.data.data_source.hospital.HospitalDataSource
import com.roger.data.data_source.hospital.HospitalRemoteDataSourceImpl
import com.roger.data.data_source.pet.PetDataSource
import com.roger.data.data_source.pet.PetLocalDataSourceImpl
import com.roger.data.data_source.pet.PetRemoteDataSourceImpl
import com.roger.data.data_source.shelter.ShelterDataSource
import com.roger.data.data_source.shelter.ShelterRemoteDataSourceImpl
import com.roger.data.data_source.user.UserDataSource
import com.roger.data.data_source.user.UserLocalDataSourceImpl
import com.roger.data.data_source.weather.WeatherDataSource
import com.roger.data.data_source.weather.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindPetRemoteDataSource(petRemoteDataSourceImpl: PetRemoteDataSourceImpl): PetDataSource.Remote

    @Binds
    abstract fun bindPetLocalDataSource(petLocalDataSourceImpl: PetLocalDataSourceImpl): PetDataSource.Local

    @Binds
    abstract fun bindUserLocalDataSource(userLocalDataSourceImpl: UserLocalDataSourceImpl): UserDataSource.Local

    @Binds
    abstract fun bindHospitalRemoteDataSource(hospitalRemoteDataSourceImpl: HospitalRemoteDataSourceImpl): HospitalDataSource.Remote

    @Binds
    abstract fun bindWeatherRemoteDataSource(weatherRemoteDataSourceImpl: WeatherRemoteDataSourceImpl): WeatherDataSource.Remote

    @Binds
    abstract fun bindShelterRemoteDataSource(shelterRemoteDataSourceImpl: ShelterRemoteDataSourceImpl): ShelterDataSource.Remote
}