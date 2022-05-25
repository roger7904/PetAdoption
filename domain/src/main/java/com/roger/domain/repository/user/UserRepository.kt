package com.roger.domain.repository.user

import com.roger.domain.entity.user.UserEntity
import com.roger.domain.use_case.user.GetUserUseCase
import com.roger.domain.use_case.user.InitUserUseCase
import io.reactivex.rxjava3.core.Single
import com.roger.domain.use_case.user.UpdateUserUseCase

interface UserRepository {
    fun initUser(param: InitUserUseCase.Param): Single<Unit>
    fun updateUser(param: UpdateUserUseCase.Param): Single<Unit>
    fun getUser(param: GetUserUseCase.Param): Single<UserEntity>
}