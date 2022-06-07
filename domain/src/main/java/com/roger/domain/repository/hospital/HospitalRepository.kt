package com.roger.domain.repository.hospital

import androidx.paging.PagingData
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.domain.use_case.hospital.GetPagingHospitalListUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface HospitalRepository {
    fun getHospitalInfo(param: GetHospitalInfoUseCase.Param): Single<List<HospitalEntity>>
    fun getHospitalInfoPagingData(param: GetPagingHospitalListUseCase.Param): Flowable<PagingData<HospitalEntity>>
}