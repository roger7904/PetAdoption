package com.roger.domain.use_case.shelter

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.repository.shelter.ShelterRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetShelterInfoUseCase(
    private val shelterRepository: ShelterRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<GetShelterInfoUseCase.Param, List<ShelterEntity>>(errorHandler) {

    override fun buildUseCase(param: Param): Single<List<ShelterEntity>> {
        return shelterRepository.getShelterInfo(param)
    }

    data class Param(
        val id: String?,
    )
}