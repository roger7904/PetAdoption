package com.roger.data.data_source.pet

import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


class PetRemoteDataSourceImpl @Inject constructor(private val petApiService: PetApiService) :
    PetRemoteDataSource {

    override fun getPetInfo(
        animalId: Int?,
        top: Int?,
        skip: Int?,
        animalKind: String?,
        animalSex: String?,
        animalBodyType: String?,
        animalColor: String?,
    ): Single<List<PetDto>> {
        return petApiService.getPetInfo(
            animal_id = animalId,
            top = top,
            skip = skip,
            animal_kind = animalKind,
            animal_sex = animalSex,
            animal_bodytype = animalBodyType,
            animal_colour = animalColor
        )
    }
}