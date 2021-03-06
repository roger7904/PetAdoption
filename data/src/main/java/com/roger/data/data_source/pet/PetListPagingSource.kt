package com.roger.data.data_source.pet

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.roger.data.mapper.pet.PetMapper
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPagingPetListUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PetListPagingSource @Inject constructor(
    private val petApiService: PetApiService,
    private val petMapper: PetMapper,
    private val param: GetPagingPetListUseCase.Param,
) : RxPagingSource<Int, PetEntity>() {

    override fun getRefreshKey(state: PagingState<Int, PetEntity>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let { anchorPage ->
                val pageIndex = state.pages.indexOf(anchorPage)
                if (pageIndex == 0) {
                    null
                } else {
                    state.pages[pageIndex - 1].nextKey
                }
            }
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, PetEntity>> {
        val position = params.key ?: 1
        val pageMapper = PetPageMapper.getPage(position)
        return petApiService
            .getPetInfo(
                null,
                pageMapper.top,
                pageMapper.skip,
                param.animalKind,
                param.animalSex,
                param.animalBodyType,
                param.animalColour)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, PetEntity>> { result ->
                val entityList = result.map {
                    petMapper.toEntity(it)
                }
                LoadResult.Page(
                    data = entityList,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (position == pageMapper.total) null else position + 1
                )
            }
            .onErrorReturn { e ->
                when (e) {
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            }
    }
}