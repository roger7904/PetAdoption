package com.roger.data.data_source.pet

import androidx.room.*
import com.roger.data.dto.pet.FavoritePetModel
import com.roger.data.local_storage.DatabaseManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface FavoritePetDaoService {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavoritePet(favoritePetModel: FavoritePetModel): Completable

    @Query(
        "DELETE FROM ${DatabaseManager.TABLE_FAVORITE_PET}" +
                " WHERE ${FavoritePetModel.COL_USER_ID} = :userId" +
                " AND ${FavoritePetModel.COL_PET_ID} = :petId"
    )
    fun deleteFavoritePet(userId: String, petId: String): Completable

    @Query(
        "SELECT * FROM ${DatabaseManager.TABLE_FAVORITE_PET}" +
                " WHERE ${FavoritePetModel.COL_USER_ID} = :userId"
    )
    fun getFavoritePetList(userId: String): Maybe<List<FavoritePetModel>>
}