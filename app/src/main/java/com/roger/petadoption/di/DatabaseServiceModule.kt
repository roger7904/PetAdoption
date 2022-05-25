package com.roger.petadoption.di

import com.roger.data.data_source.user.UserDaoService
import com.roger.data.local_storage.DatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseServiceModule {

    @Singleton
    @Provides
    fun provideUserDaoService(db: DatabaseManager): UserDaoService {
        return db.userDaoService()
    }
}