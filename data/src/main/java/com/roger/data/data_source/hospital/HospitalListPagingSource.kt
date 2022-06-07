package com.roger.data.data_source.hospital

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.roger.data.mapper.hospital.HospitalMapper
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetPagingHospitalListUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HospitalListPagingSource @Inject constructor(
    private val hospitalApiService: HospitalApiService,
    private val hospitalMapper: HospitalMapper,
    private val param: GetPagingHospitalListUseCase.Param,
) : RxPagingSource<Int, HospitalEntity>() {

    override fun getRefreshKey(state: PagingState<Int, HospitalEntity>): Int? {
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

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, HospitalEntity>> {
        val position = params.key ?: 1
        val pageMapper = HospitalPageMapper.getPage(position)
        return hospitalApiService
            .getHospitalInfo(
                pageMapper.top,
                pageMapper.skip,
                param.filter,
            )
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, HospitalEntity>> { result ->
                val entityList = result.map {
                    hospitalMapper.toEntity(it)
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