package com.roger.data.repository.user

import com.roger.data.data_source.user.UserDataSource
import com.roger.data.dto.user.UserModel
import com.roger.domain.entity.user.UserEntity
import com.roger.domain.repository.user.UserRepository
import com.roger.domain.use_case.user.GetUserUseCase
import com.roger.domain.use_case.user.InitUserUseCase
import com.roger.domain.use_case.user.UpdateUserUseCase
import io.reactivex.rxjava3.core.*
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserDataSource.Local,
) : UserRepository {
    override fun initUser(param: InitUserUseCase.Param): Single<Unit> {
        return userLocalDataSource.initUser(
            UserModel(
                param.userId,
                param.userName
            )
        ).toSingle {}
    }

    override fun updateUser(param: UpdateUserUseCase.Param): Single<Unit> {
        return userLocalDataSource.updateUser(
            UserModel(
                param.userId,
                param.userName
            )
        ).toSingle {}
    }

    override fun getUser(param: GetUserUseCase.Param): Single<UserEntity> {
        return userLocalDataSource.getUser(param.userId).map {
            UserEntity(
                it.userId,
                it.userName
            )
        }.toSingle()
    }
}