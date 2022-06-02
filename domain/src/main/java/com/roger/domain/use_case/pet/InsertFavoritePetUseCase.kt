package com.roger.domain.use_case.pet

import com.roger.domain.common.ErrorHandler
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class InsertFavoritePetUseCase(
    private val petRepository: PetRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<InsertFavoritePetUseCase.Param, Unit>(errorHandler) {

    override fun buildUseCase(param: Param): Single<Unit> {
        return petRepository.insertFavoritePet(param)
    }

    data class Param(
        val id: String,
        val userId: String,
        val petId: Int
    )
}