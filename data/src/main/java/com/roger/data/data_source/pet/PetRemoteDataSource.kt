package com.roger.data.data_source.pet

import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single

interface PetRemoteDataSource {
    fun getPetInfo(): Single<List<PetDto>>
}