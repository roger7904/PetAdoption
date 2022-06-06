package com.roger.data.local_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roger.data.data_source.pet.FavoritePetDaoService
import com.roger.data.data_source.user.UserDaoService
import com.roger.data.dto.pet.FavoritePetModel
import com.roger.data.dto.user.UserModel

@Database(
    entities = [
        UserModel::class,
        FavoritePetModel::class,
    ],
    version = 1
)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun userDaoService(): UserDaoService
    abstract fun favoritePetDaoService(): FavoritePetDaoService

    companion object {
        const val TABLE_USER = "User"
        const val TABLE_FAVORITE_PET = "FavoritePet"
    }
}