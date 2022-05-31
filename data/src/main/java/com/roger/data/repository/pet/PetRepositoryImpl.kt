package com.roger.data.repository.pet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.roger.data.data_source.pet.PetApiService
import com.roger.data.data_source.pet.PetDataSource
import com.roger.data.data_source.pet.PetListPagingSource
import com.roger.data.dto.pet.FavoritePetModel
import com.roger.data.mapper.pet.PetMapper
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.repository.pet.PetRepository
import com.roger.domain.use_case.pet.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(
    private val petRemoteDataSource: PetDataSource.Remote,
    private val petLocalDataSource: PetDataSource.Local,
    private val petMapper: PetMapper,
    private val petApiService: PetApiService,
) : PetRepository {
    override fun insertFavoritePet(param: InsertFavoritePetUseCase.Param): Single<Unit> {
        return petLocalDataSource.insertFavoritePet(
            FavoritePetModel(
                param.id,
                param.userId,
                param.petId
            )
        ).toSingle {}
    }

    override fun deleteFavoritePet(param: DeleteFavoritePetUseCase.Param): Single<Unit> {
        return petLocalDataSource.deleteFavoritePet(param.userId, param.petId).toSingle {}
    }

    override fun getFavoritePetList(param: GetFavoritePetListUseCase.Param): Single<List<FavoritePetEntity>> {
        return petLocalDataSource.getFavoritePetList(param.userId).map {
            it.map { model ->
                FavoritePetEntity(
                    id = model.id,
                    userId = model.userId,
                    petId = model.petId
                )
            }
        }.toSingle()
    }

    override fun getPetInfo(param: GetPetInfoUseCase.Param): Single<List<PetEntity>> {
        return petRemoteDataSource.getPetInfo(
            param.animalId,
            param.top,
            param.skip,
            param.animalKind,
            param.animalSex,
            param.animalBodyType,
            param.animalColour
        ).map { list ->
            list.map {
                petMapper.toEntity(it)
            }
        }
    }

    override fun getPetInfoPagingData(param: GetPagingPetListUseCase.Param): Flowable<PagingData<PetEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            pagingSourceFactory = {
                PetListPagingSource(petApiService, petMapper, param)
            }
        ).flowable
    }

}