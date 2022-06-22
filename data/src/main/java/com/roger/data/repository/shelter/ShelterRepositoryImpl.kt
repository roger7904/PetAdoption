package com.roger.data.repository.shelter

import com.roger.data.data_source.shelter.ShelterApiService
import com.roger.data.data_source.shelter.ShelterDataSource
import com.roger.data.mapper.shelter.ShelterMapper
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.repository.shelter.ShelterRepository
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
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

}