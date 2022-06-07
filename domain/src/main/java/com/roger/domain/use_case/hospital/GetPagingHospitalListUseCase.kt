package com.roger.domain.use_case.hospital

import androidx.paging.PagingData
import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.use_case.ParamFlowableUseCase
import io.reactivex.rxjava3.core.Flowable

class GetPagingHospitalListUseCase(
    private val hospitalRepository: HospitalRepository,
    errorHandler: ErrorHandler,
) : ParamFlowableUseCase<GetPagingHospitalListUseCase.Param, PagingData<HospitalEntity>>(
    errorHandler) {

    override fun buildUseCase(param: Param): Flowable<PagingData<HospitalEntity>> {
        return hospitalRepository.getHospitalInfoPagingData(param)
    }

    data class Param(
        val filter: String?,
    )
}