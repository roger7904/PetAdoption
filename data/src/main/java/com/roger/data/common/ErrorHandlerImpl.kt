package com.roger.data.common

import com.google.gson.Gson
import com.roger.data.dto.HttpError
import com.roger.domain.common.DomainError
import com.roger.domain.common.ErrorHandler
import okio.Buffer
import retrofit2.HttpException
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor(private val gson: Gson) : ErrorHandler {

    override fun parseError(statusCode: Int, message: String?, data: Any?): DomainError {
        return when (statusCode) {
            else -> DomainError.CommonError(statusCode, message)
        }
    }

    override fun parseThrowable(throwable: Throwable?): DomainError {
        return when (throwable) {
            is DomainError -> throwable

            is HttpException -> parseHttpError(throwable)

            else -> DomainError.CommonError()
        }
    }

    private fun parseHttpError(httpException: HttpException): DomainError {
        val response = httpException.response()
        val errorJson = (response?.errorBody()?.source() as? Buffer)?.copy()?.readUtf8()
        return try {
            val httpError = gson.fromJson(errorJson, HttpError::class.java)
            val httpStatusCode = httpException.code()

            when (val message = httpError.message) {
                null ->
                    DomainError.UnknownError(httpException, httpStatusCode)

                else ->
                    DomainError.CommonError(httpStatusCode, message)
            }
        } catch (e: Exception) {
            DomainError.UnknownError(httpException)
        }
    }
}