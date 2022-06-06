package com.roger.domain.repository.hospital

import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import io.reactivex.rxjava3.core.Single

interface HospitalRepository {
    fun getHospitalInfo(param: GetHospitalInfoUseCase.Param): Single<List<HospitalEntity>>
}