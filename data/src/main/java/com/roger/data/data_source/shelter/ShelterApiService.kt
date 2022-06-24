package com.roger.data.data_source.shelter

import com.roger.data.dto.shelter.ShelterDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ShelterApiService {

    @GET(endpoint)
    fun getShelterInfo(
        @Query("\$top") top: Int?,
        @Query("\$skip") skip: Int?,
        @Query("CityName") cityName: String?,
        @Query("ID") id: String?,
    ): Single<List<ShelterDto>>

    companion object {
        private const val endpoint = "TransService.aspx?UnitId=2thVboChxuKs"
    }
}