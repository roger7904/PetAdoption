package com.roger.domain.repository.shelter

import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
import io.reactivex.rxjava3.core.Single

interface ShelterRepository {
    fun getShelterInfo(param: GetShelterInfoUseCase.Param): Single<List<ShelterEntity>>
}