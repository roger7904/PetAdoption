package com.roger.domain.repository.pet

import androidx.paging.PagingData
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface PetRepository {
    fun insertFavoritePet(param: InsertFavoritePetUseCase.Param): Single<Unit>
    fun deleteFavoritePet(param: DeleteFavoritePetUseCase.Param): Single<Unit>
    fun getFavoritePetList(param: GetFavoritePetListUseCase.Param): Single<List<FavoritePetEntity>>
    fun getPetInfo(param: GetPetInfoUseCase.Param): Single<List<PetEntity>>
    fun getPetInfoPagingData(param: GetPagingPetListUseCase.Param): Flowable<PagingData<PetEntity>>
}