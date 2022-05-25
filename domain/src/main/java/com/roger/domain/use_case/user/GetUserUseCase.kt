package com.roger.domain.use_case.user

import com.roger.domain.common.ErrorHandler
import com.roger.domain.entity.user.UserEntity
import com.roger.domain.repository.user.UserRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class GetUserUseCase(
    private val userRepository: UserRepository,
    val errorHandler: ErrorHandler
) : ParamSingleUseCase<GetUserUseCase.Param, UserEntity>(errorHandler) {

    override fun buildUseCase(param: Param): Single<UserEntity> {
        return userRepository.getUser(param)
    }

    data class Param(
        val userId: String
    )
}