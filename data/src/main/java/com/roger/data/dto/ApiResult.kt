package com.roger.data.dto

data class ApiResult<T>(val status: Int, val message: String, val data: T?)