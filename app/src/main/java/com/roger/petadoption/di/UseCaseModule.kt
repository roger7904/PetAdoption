package com.roger.petadoption.di

import com.roger.domain.common.ErrorHandler
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.repository.shelter.ShelterRepository
import com.roger.domain.repository.user.UserRepository
import com.roger.domain.repository.weather.WeatherRepository
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.domain.use_case.hospital.GetPagingHospitalListUseCase
import com.roger.domain.use_case.pet.*
import com.roger.domain.use_case.shelter.GetPagingShelterListUseCase
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
import com.roger.domain.use_case.user.GetUserUseCase
import com.roger.domain.use_case.user.InitUserUseCase
import com.roger.domain.use_case.user.UpdateUserUseCase
import com.roger.domain.use_case.weather.GetWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetPetInfoUseCase(
        petRepository: PetRepository,
        errorHandler: ErrorHandler,
    ): GetPetInfoUseCase {
        return GetPetInfoUseCase(petRepository, errorHandler)
    }

    @Provides
    fun provideInitUserUseCase(
        userRepository: UserRepository,
        errorHandler: ErrorHandler,
    ): InitUserUseCase {
        return InitUserUseCase(userRepository, errorHandler)
    }

    @Provides
    fun provideUpdateUserUseCase(
        userRepository: UserRepository,
        errorHandler: ErrorHandler,
    ): UpdateUserUseCase {
        return UpdateUserUseCase(userRepository, errorHandler)
    }

    @Provides
    fun provideGetUserUseCase(
        userRepository: UserRepository,
        errorHandler: ErrorHandler,
    ): GetUserUseCase {
        return GetUserUseCase(userRepository, errorHandler)
    }

    @Provides
    fun provideGetPagingPetListUseCase(
        petRepository: PetRepository,
        errorHandler: ErrorHandler,
    ): GetPagingPetListUseCase {
        return GetPagingPetListUseCase(petRepository, errorHandler)
    }

    @Provides
    fun provideInsertFavoritePetUseCase(
        petRepository: PetRepository,
        errorHandler: ErrorHandler,
    ): InsertFavoritePetUseCase {
        return InsertFavoritePetUseCase(petRepository, errorHandler)
    }

    @Provides
    fun provideDeleteFavoritePetUseCase(
        petRepository: PetRepository,
        errorHandler: ErrorHandler,
    ): DeleteFavoritePetUseCase {
        return DeleteFavoritePetUseCase(petRepository, errorHandler)
    }

    @Provides
    fun provideGetFavoritePetListUseCase(
        petRepository: PetRepository,
        errorHandler: ErrorHandler,
    ): GetFavoritePetListUseCase {
        return GetFavoritePetListUseCase(petRepository, errorHandler)
    }

    @Provides
    fun provideGetHospitalInfoUseCase(
        hospitalRepository: HospitalRepository,
        errorHandler: ErrorHandler,
    ): GetHospitalInfoUseCase {
        return GetHospitalInfoUseCase(hospitalRepository, errorHandler)
    }

    @Provides
    fun provideGetPagingHospitalListUseCase(
        hospitalRepository: HospitalRepository,
        errorHandler: ErrorHandler,
    ): GetPagingHospitalListUseCase {
        return GetPagingHospitalListUseCase(hospitalRepository, errorHandler)
    }

    @Provides
    fun provideGetWeatherUseCase(
        weatherRepository: WeatherRepository,
        errorHandler: ErrorHandler,
    ): GetWeatherUseCase {
        return GetWeatherUseCase(weatherRepository, errorHandler)
    }

    @Provides
    fun provideGetShelterInfoUseCase(
        shelterRepository: ShelterRepository,
        errorHandler: ErrorHandler,
    ): GetShelterInfoUseCase {
        return GetShelterInfoUseCase(shelterRepository, errorHandler)
    }

    @Provides
    fun provideGetPagingShelterListUseCase(
        shelterRepository: ShelterRepository,
        errorHandler: ErrorHandler,
    ): GetPagingShelterListUseCase {
        return GetPagingShelterListUseCase(shelterRepository, errorHandler)
    }
}