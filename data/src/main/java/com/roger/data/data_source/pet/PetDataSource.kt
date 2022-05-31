package com.roger.data.data_source.pet

import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single

interface PetDataSource {
    interface Local {

    }

    interface Remote {
        fun getPetInfo(
            animalId: Int?,
            top: Int?,
            skip: Int?,
            animalKind: String?,
            animalSex: String?,
            animalBodyType: String?,
            animalColor: String?,
        ): Single<List<PetDto>>
    }
}