package com.roger.petadoption.di

import com.roger.data.data_source.pet.PetRemoteDataSource
import com.roger.data.data_source.pet.PetRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindPetRemoteDataSource(petRemoteDataSourceImpl: PetRemoteDataSourceImpl): PetRemoteDataSource
}