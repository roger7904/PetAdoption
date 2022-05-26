package com.roger.domain.repository.pet

import androidx.paging.PagingData
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPagingPetListUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface PetRepository {
    fun getPetInfo(): Single<List<PetEntity>>
    fun getPetInfoPagingData(param: GetPagingPetListUseCase.Param): Flowable<PagingData<PetEntity>>
}