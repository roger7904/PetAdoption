package com.roger.data.data_source.hospital

import com.roger.data.dto.hospital.HospitalDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HospitalRemoteDataSourceImpl @Inject constructor(private val hospitalApiService: HospitalApiService) :
    HospitalDataSource.Remote {

    override fun getHospitalInfo(
        top: Int?,
        skip: Int?,
        filter: String?,
    ): Single<List<HospitalDto>> {
        return hospitalApiService.getHospitalInfo(
            top = top,
            skip = skip,
            filter = filter,
        )
    }
}