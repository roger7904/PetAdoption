package com.roger.data.repository.shelter

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.roger.data.data_source.shelter.ShelterApiService
import com.roger.data.data_source.shelter.ShelterDataSource
import com.roger.data.data_source.shelter.ShelterListPagingSource
import com.roger.data.mapper.shelter.ShelterMapper
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.repository.shelter.ShelterRepository
import com.roger.domain.use_case.shelter.GetPagingShelterListUseCase
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ShelterRepositoryImpl @Inject constructor(
    private val shelterApiService: ShelterApiService,
    private val shelterRemoteDataSource: ShelterDataSource.Remote,
    private val shelterMapper: ShelterMapper,
) : ShelterRepository {

    override fun getShelterInfo(param: GetShelterInfoUseCase.Param): Single<List<ShelterEntity>> {
        return shelterRemoteDataSource.getShelterInfo(
            null, null, null, param.id
        ).map { list ->
            list.map {
                shelterMapper.toEntity(it)
            }
        }
    }

    override fun getShelterInfoPagingData(param: GetPagingShelterListUseCase.Param): Flowable<PagingData<ShelterEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                ShelterListPagingSource(shelterApiService, shelterMapper, param)
            }
        ).flowable
    }
}