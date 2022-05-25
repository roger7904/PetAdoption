package com.roger.petadoption.di

import android.content.Context
import androidx.room.Room
import com.roger.data.local_storage.DatabaseManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Singleton
    @Provides
    fun provideDatabaseManager(@ApplicationContext context: Context): DatabaseManager {
        return Room.databaseBuilder(
            context,
            DatabaseManager::class.java,
            DatabaseManager::class.java.simpleName
        ).build()
    }
}