package com.roger.domain.use_case.pet

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.SingleUseCase
import io.reactivex.rxjava3.core.Single

class GetPetInfoUseCase(
    private val petRepository: PetRepository,
    errorHandler: ErrorHandler
) : SingleUseCase<List<PetEntity>>(errorHandler) {

    override fun buildUseCase(): Single<List<PetEntity>> {
        return petRepository.getPetInfo()
    }
}