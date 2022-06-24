package com.roger.domain.repository.shelter

import androidx.paging.PagingData
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.use_case.shelter.GetPagingShelterListUseCase
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface ShelterRepository {
    fun getShelterInfo(param: GetShelterInfoUseCase.Param): Single<List<ShelterEntity>>
    fun getShelterInfoPagingData(param: GetPagingShelterListUseCase.Param): Flowable<PagingData<ShelterEntity>>
}