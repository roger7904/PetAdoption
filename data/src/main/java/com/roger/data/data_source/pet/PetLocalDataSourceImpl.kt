package com.roger.data.data_source.pet

import com.roger.data.dto.pet.FavoritePetModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class PetLocalDataSourceImpl @Inject constructor(
    private val favoritePetDaoService: FavoritePetDaoService,
) : PetDataSource.Local {
    override fun insertFavoritePet(favoritePetModel: FavoritePetModel): Completable {
        return favoritePetDaoService.insertFavoritePet(favoritePetModel)
    }

    override fun deleteFavoritePet(userId: String, petId: Int): Completable {
        return favoritePetDaoService.deleteFavoritePet(userId, petId)
    }

    override fun getFavoritePetList(userId: String): Maybe<List<FavoritePetModel>> {
        return favoritePetDaoService.getFavoritePetList(userId)
    }

}