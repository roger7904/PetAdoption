package com.roger.data.data_source.shelter

import com.roger.data.dto.shelter.ShelterDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ShelterRemoteDataSourceImpl @Inject constructor(private val shelterApiService: ShelterApiService) :
    ShelterDataSource.Remote {

    override fun getShelterInfo(
        top: Int?,
        skip: Int?,
        cityName: String?,
        id: String?,
    ): Single<List<ShelterDto>> {
        return shelterApiService.getShelterInfo(
            top, skip, cityName, id
        )
    }
}