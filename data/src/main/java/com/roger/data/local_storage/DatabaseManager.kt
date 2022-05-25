package com.roger.data.local_storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roger.data.data_source.user.UserDaoService
import com.roger.data.dto.user.UserModel

@Database(
    entities = [
        UserModel::class,
    ],
    version = 1
)
abstract class DatabaseManager : RoomDatabase() {

    abstract fun userDaoService(): UserDaoService

    companion object {
        const val TABLE_USER = "User"
    }
}