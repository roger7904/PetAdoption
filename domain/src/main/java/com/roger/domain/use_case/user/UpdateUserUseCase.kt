package com.roger.domain.use_case.user

import com.roger.domain.common.ErrorHandler
import com.roger.domain.repository.user.UserRepository
import com.roger.domain.use_case.ParamSingleUseCase
import io.reactivex.rxjava3.core.Single

class UpdateUserUseCase(
    private val userRepository: UserRepository,
    val errorHandler: ErrorHandler
) : ParamSingleUseCase<UpdateUserUseCase.Param, Unit>(errorHandler) {

    override fun buildUseCase(param: Param): Single<Unit> {
        return userRepository.updateUser(param)
    }

    data class Param(
        val userId: String,
        val userName: String,
    )
}