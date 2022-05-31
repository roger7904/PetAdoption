package com.roger.data.data_source.pet

import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface PetApiService {

    @GET(endpoint)
    fun getPetInfo(
        @Query("animal_id") animal_id: Int?,
        @Query("\$top") top: Int?,
        @Query("\$skip") skip: Int?,
        @Query("animal_kind") animal_kind: String?,
        @Query("animal_sex") animal_sex: String?,
        @Query("animal_bodytype") animal_bodytype: String?,
        @Query("animal_colour") animal_colour: String?,
    ): Single<List<PetDto>>

    companion object {
        private const val endpoint = "TransService.aspx?UnitId=QcbUEzN6E6DL"
    }
}