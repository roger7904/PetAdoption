package com.roger.domain.use_case.pet

import com.roger.domain.common.ErrorHandler
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class DeleteFavoritePetUseCase(
    private val petRepository: PetRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<DeleteFavoritePetUseCase.Param, Unit>(errorHandler) {

    override fun buildUseCase(param: Param): Single<Unit> {
        return petRepository.deleteFavoritePet(param)
    }

    data class Param(
        val userId: String,
        val petId: Int,
    )
}