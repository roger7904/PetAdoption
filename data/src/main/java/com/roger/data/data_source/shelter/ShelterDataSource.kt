package com.roger.data.data_source.shelter

import com.roger.data.dto.shelter.ShelterDto
import io.reactivex.rxjava3.core.Single

interface ShelterDataSource {
    interface Remote {
        fun getShelterInfo(
            top: Int?,
            skip: Int?,
            cityName: String?,
            id: String?,
        ): Single<List<ShelterDto>>
    }
}