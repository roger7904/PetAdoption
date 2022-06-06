package com.roger.data.data_source.hospital

import com.roger.data.dto.hospital.HospitalDto
import io.reactivex.rxjava3.core.Single

interface HospitalDataSource {
    interface Remote {
        fun getHospitalInfo(
            top: Int?,
            skip: Int?,
            filter: String?,
        ): Single<List<HospitalDto>>
    }
}