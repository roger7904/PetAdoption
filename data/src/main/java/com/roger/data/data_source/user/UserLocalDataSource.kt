package com.roger.data.data_source.user

import com.roger.data.dto.user.UserModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDaoService: UserDaoService
) : UserDataSource.Local {
    override fun initUser(user: UserModel): Completable {
        return userDaoService.initUser(user)
    }

    override fun updateUser(user: UserModel): Completable {
        return userDaoService.updateUser(user)
    }

    override fun getUser(userId: String): Maybe<UserModel> {
        return userDaoService.getUser(userId)
    }
}