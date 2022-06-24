package com.roger.domain.use_case.shelter

import androidx.paging.PagingData
import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.repository.shelter.ShelterRepository
import com.roger.domain.use_case.ParamFlowableUseCase
import io.reactivex.rxjava3.core.Flowable

class GetPagingShelterListUseCase(
    private val shelterRepository: ShelterRepository,
    errorHandler: ErrorHandler,
) : ParamFlowableUseCase<GetPagingShelterListUseCase.Param, PagingData<ShelterEntity>>(
    errorHandler) {

    override fun buildUseCase(param: Param): Flowable<PagingData<ShelterEntity>> {
        return shelterRepository.getShelterInfoPagingData(param)
    }

    data class Param(
        val cityName: String?,
    )
}