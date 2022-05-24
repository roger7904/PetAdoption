package com.roger.domain.use_case

import com.roger.domain.common.DataResult
import com.roger.domain.common.ErrorHandler
import io.reactivex.rxjava3.core.Single

abstract class ParamSingleUseCase<P, R>(private val errorHandler: ErrorHandler) {

    operator fun invoke(param: P): Single<DataResult<R>> {
        return buildUseCase(param)
            .map<DataResult<R>> {
                DataResult.Success(it)
            }.onErrorReturn {
                DataResult.Failure(errorHandler.parseThrowable(it))
            }
    }

    abstract fun buildUseCase(param: P): Single<R>
}