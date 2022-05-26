package com.roger.data.repository.pet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.roger.data.data_source.pet.PetApiService
import com.roger.data.data_source.pet.PetListPagingSource
import com.roger.data.data_source.pet.PetRemoteDataSource
import com.roger.data.mapper.pet.PetMapper
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.pet.GetPagingPetListUseCase
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petRemoteDataSource: PetRemoteDataSource,
    private val petMapper: PetMapper,
    private val petApiService: PetApiService
) : PetRepository {
    override fun getPetInfo(): Single<List<PetEntity>> {
        return petRemoteDataSource.getPetInfo().map { list ->
            list.data?.map {
                petMapper.toEntity(it)
            }
        }
    }

    override fun getPetInfoPagingData(param: GetPagingPetListUseCase.Param): Flowable<PagingData<PetEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 12,
            ),
            pagingSourceFactory = {
                PetListPagingSource(petApiService, petMapper, param)
            }
        ).flowable
    }

}