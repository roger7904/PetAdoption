package com.roger.petadoption.ui.base

sealed class ViewEvent {
    object None : ViewEvent()
    object Loading : ViewEvent()
    object Done : ViewEvent()

    // Error
    data class Error(val message: String?) : ViewEvent()
    data class UnknownError(val error: Throwable?) : ViewEvent()

    abstract class Data : ViewEvent()

    //Data event should inherit this class
    abstract class ViewEventData<T>(val data: T? = null) : ViewEvent.Data()
}