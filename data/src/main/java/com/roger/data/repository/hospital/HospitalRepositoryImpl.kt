package com.roger.data.repository.hospital

import com.roger.data.data_source.hospital.HospitalDataSource
import com.roger.data.mapper.hospital.HospitalMapper
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val hospitalRemoteDataSource: HospitalDataSource.Remote,
    private val hospitalMapper: HospitalMapper,
) : HospitalRepository {

    override fun getHospitalInfo(param: GetHospitalInfoUseCase.Param): Single<List<HospitalEntity>> {
        return hospitalRemoteDataSource.getHospitalInfo(
            param.top,
            param.skip,
            param.filter
        ).map { list ->
            list.map {
                hospitalMapper.toEntity(it)
            }
        }
    }
}