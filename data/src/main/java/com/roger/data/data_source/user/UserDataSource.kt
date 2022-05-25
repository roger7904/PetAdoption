package com.roger.data.data_source.user

import com.roger.data.dto.user.UserModel
import io.reactivex.rxjava3.core.*

interface UserDataSource {

    interface Local {
        fun initUser(user: UserModel): Completable
        fun updateUser(user: UserModel): Completable
        fun getUser(userId: String): Maybe<UserModel>
    }
}