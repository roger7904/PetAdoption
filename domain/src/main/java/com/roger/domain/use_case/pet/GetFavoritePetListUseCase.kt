package com.roger.domain.use_case.pet

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetFavoritePetListUseCase(
    private val petRepository: PetRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<GetFavoritePetListUseCase.Param, List<FavoritePetEntity>>(errorHandler) {

    override fun buildUseCase(param: Param): Single<List<FavoritePetEntity>> {
        return petRepository.getFavoritePetList(param)
    }

    data class Param(
        val userId: String,
    )
}