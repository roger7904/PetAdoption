package com.roger.data.data_source.user

import androidx.room.*
import com.roger.data.dto.user.UserModel
import com.roger.data.local_storage.DatabaseManager
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface UserDaoService {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun initUser(userModel: UserModel): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(userModel: UserModel): Completable

    @Query(
        "SELECT * FROM ${DatabaseManager.TABLE_USER}" +
                " WHERE ${UserModel.COL_USER_ID} = :userId"
    )
    fun getUser(userId: String): Maybe<UserModel>
}