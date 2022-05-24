package com.roger.domain.common

interface ErrorHandler {
    fun parseError(statusCode: Int, message: String?, data: Any?): DomainError
    fun parseThrowable(throwable: Throwable?): DomainError
}