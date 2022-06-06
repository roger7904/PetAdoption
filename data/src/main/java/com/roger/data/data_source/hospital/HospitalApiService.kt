package com.roger.data.data_source.hospital

import com.roger.data.dto.hospital.HospitalDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface HospitalApiService {

    @GET(endpoint)
    fun getHospitalInfo(
        @Query("\$top") top: Int?,
        @Query("\$skip") skip: Int?,
        @Query("\$filter") filter: String?,
    ): Single<List<HospitalDto>>

    companion object {
        private const val endpoint = "DataFileService.aspx?UnitId=078"
    }
}