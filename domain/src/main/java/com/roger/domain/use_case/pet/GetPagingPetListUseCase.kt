package com.roger.domain.use_case.pet

import androidx.paging.PagingData
import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.ParamFlowableUseCase
import io.reactivex.rxjava3.core.Flowable

class GetPagingPetListUseCase(
    private val petRepository: PetRepository,
    errorHandler: ErrorHandler
) : ParamFlowableUseCase<GetPagingPetListUseCase.Param, PagingData<PetEntity>>(errorHandler) {

    override fun buildUseCase(param: Param): Flowable<PagingData<PetEntity>> {
        return petRepository.getPetInfoPagingData(param)
    }

    data class Param(
        val animalKind: String?,
        val animalSex: String?,
        val animalBodyType: String?,
        val animalColour: String?
    )
}