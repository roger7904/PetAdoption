package com.roger.data.repository.hospital

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.roger.data.data_source.hospital.HospitalApiService
import com.roger.data.data_source.hospital.HospitalDataSource
import com.roger.data.data_source.hospital.HospitalListPagingSource
import com.roger.data.mapper.hospital.HospitalMapper
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.repository.hospital.HospitalRepository
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.domain.use_case.hospital.GetPagingHospitalListUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val hospitalApiService: HospitalApiService,
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

    override fun getHospitalInfoPagingData(param: GetPagingHospitalListUseCase.Param): Flowable<PagingData<HospitalEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                HospitalListPagingSource(hospitalApiService, hospitalMapper, param)
            }
        ).flowable
    }
}