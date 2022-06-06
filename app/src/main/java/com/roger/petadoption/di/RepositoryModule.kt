package com.roger.petadoption.di

import com.roger.data.repository.hospital.HospitalRepositoryImpl
import com.roger.data.repository.pet.PetRepositoryImpl
import com.roger.data.repository.user.UserRepositoryImpl
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindPetRepository(petRepositoryImpl: PetRepositoryImpl): PetRepository

    @Singleton
    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Singleton
    @Binds
    abstract fun bindHospitalRepository(hospitalRepositoryImpl: HospitalRepositoryImpl): HospitalRepository
}