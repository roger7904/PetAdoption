package com.roger.petadoption.di

import com.roger.domain.common.ErrorHandler
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.pet.GetPetInfoUseCase
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
        errorHandler: ErrorHandler
    ): GetPetInfoUseCase {
        return GetPetInfoUseCase(petRepository, errorHandler)
    }

}