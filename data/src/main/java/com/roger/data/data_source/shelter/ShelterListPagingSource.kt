package com.roger.data.data_source.shelter

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.roger.data.mapper.shelter.ShelterMapper
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.use_case.shelter.GetPagingShelterListUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ShelterListPagingSource @Inject constructor(
    private val shelterApiService: ShelterApiService,
    private val shelterMapper: ShelterMapper,
    private val param: GetPagingShelterListUseCase.Param,
) : RxPagingSource<Int, ShelterEntity>() {

    override fun getRefreshKey(state: PagingState<Int, ShelterEntity>): Int? {
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

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, ShelterEntity>> {
        val position = params.key ?: 1
        val pageMapper = ShelterPageMapper.getPage(position)
        return shelterApiService
            .getShelterInfo(
                pageMapper.top,
                pageMapper.skip,
                param.cityName,
                null
            )
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, ShelterEntity>> { result ->
                val entityList = result.map {
                    shelterMapper.toEntity(it)
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