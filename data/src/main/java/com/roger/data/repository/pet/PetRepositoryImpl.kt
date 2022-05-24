package com.roger.data.repository.pet

import com.roger.data.data_source.pet.PetRemoteDataSource
import com.roger.data.mapper.pet.PetMapper
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petRemoteDataSource: PetRemoteDataSource,
    private val petMapper: PetMapper
) : PetRepository {
    override fun getPetInfo(): Single<List<PetEntity>> {
        return petRemoteDataSource.getPetInfo().map { list ->
            list.data?.map {
                petMapper.toEntity(it)
            }
        }
    }
}