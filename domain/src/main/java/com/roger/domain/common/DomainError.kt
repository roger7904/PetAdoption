package com.roger.domain.common

sealed class DomainError(throwable: Throwable? = null) : Throwable() {

    data class CommonError(val errorMessage: String? = null) : DomainError()

    data class UnknownError(val throwable: Throwable?, val httpStatusCode: Int? = null) :
        DomainError(throwable)
}