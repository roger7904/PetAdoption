package com.roger.data.data_source.pet

import com.roger.data.dto.pet.FavoritePetModel
import com.roger.data.dto.pet.PetDto
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface PetDataSource {
    interface Local {
        fun insertFavoritePet(favoritePetModel: FavoritePetModel): Completable
        fun deleteFavoritePet(userId: String, petId: Int): Completable
        fun getFavoritePetList(userId: String): Maybe<List<FavoritePetModel>>
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