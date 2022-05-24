package com.roger.domain.use_case

import com.roger.domain.common.DataResult
import com.roger.domain.common.ErrorHandler
import io.reactivex.rxjava3.core.Single

abstract class SingleUseCase<R>(private val errorHandler: ErrorHandler) {

    operator fun invoke(): Single<DataResult<R>> {
        return buildUseCase()
            .map<DataResult<R>> {
                DataResult.Success(it)
            }.onErrorReturn {
                DataResult.Failure(errorHandler.parseThrowable(it))
            }
    }

    abstract fun buildUseCase(): Single<R>
}