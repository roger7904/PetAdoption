package com.roger.petadoption.di

import com.roger.data.data_source.pet.PetDataSource
import com.roger.data.data_source.pet.PetRemoteDataSourceImpl
import com.roger.data.data_source.user.UserDataSource
import com.roger.data.data_source.user.UserLocalDataSource
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
    abstract fun bindUserLocalDataSource(userLocalDataSource: UserLocalDataSource): UserDataSource.Local
}