package com.roger.domain.use_case.pet

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetPetInfoUseCase(
    private val petRepository: PetRepository,
    errorHandler: ErrorHandler,
) : ParamSingleUseCase<GetPetInfoUseCase.Param, List<PetEntity>>(errorHandler) {

    override fun buildUseCase(param: Param): Single<List<PetEntity>> {
        return petRepository.getPetInfo(param)
    }

    data class Param(
        val animalId: Int?,
        val top: Int?,
        val skip: Int?,
        val animalKind: String?,
        val animalSex: String?,
        val animalBodyType: String?,
        val animalColour: String?
    )
}