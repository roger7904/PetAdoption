package com.roger.petadoption.ui.base

sealed class ViewEvent {
    object Loading : ViewEvent()
    object Done : ViewEvent()

    // Error
    data class Error(val message: String?) : ViewEvent()
    data class UnknownError(val error: Throwable?) : ViewEvent()
}