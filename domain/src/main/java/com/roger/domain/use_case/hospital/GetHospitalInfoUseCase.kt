package com.roger.domain.use_case.hospital

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetHospitalInfoUseCase(
    private val hospitalRepository: HospitalRepository,
    val errorHandler: ErrorHandler,
) : ParamSingleUseCase<GetHospitalInfoUseCase.Param, List<HospitalEntity>>(errorHandler) {

    override fun buildUseCase(param: Param): Single<List<HospitalEntity>> {
        return hospitalRepository.getHospitalInfo(param)
    }

    data class Param(
        val top: Int?,
        val skip: Int?,
        val filter: String?,
    )
}