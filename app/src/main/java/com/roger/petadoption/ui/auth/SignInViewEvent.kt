package com.roger.petadoption.ui.auth

import com.roger.petadoption.ui.base.ViewEvent

sealed class SignInViewEvent {
    object LoginSuccess : ViewEvent.Data()
    object LoginFail : ViewEvent.Data()
    object PasswordLoginFail : ViewEvent.Data()
}