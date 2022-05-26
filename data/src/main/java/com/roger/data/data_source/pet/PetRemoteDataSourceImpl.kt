package com.roger.data.data_source.pet

import com.roger.data.dto.ApiResult
import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class PetRemoteDataSourceImpl @Inject constructor(private val petApiService: PetApiService) :
    PetRemoteDataSource {

    override fun getPetInfo(): Single<ApiResult<List<PetDto>>> {
        return petApiService.getPetInfo(10, 0, null, null)
    }
}