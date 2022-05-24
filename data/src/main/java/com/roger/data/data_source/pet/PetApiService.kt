package com.roger.data.data_source.pet

import com.roger.data.dto.ApiResult
import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface PetApiService {

    @GET(endpoint)
    fun getPetInfo(): Single<ApiResult<List<PetDto>>>

    companion object {
        private const val endpoint = "TransService.aspx?UnitId=QcbUEzN6E6DL"
    }
}