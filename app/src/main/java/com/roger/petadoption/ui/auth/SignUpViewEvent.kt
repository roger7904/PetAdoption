package com.roger.petadoption.ui.auth

import com.roger.petadoption.ui.base.ViewEvent

sealed class SignUpViewEvent {
    object SignUpSuccess : ViewEvent.Data()
    object SignUpFail : ViewEvent.Data()
}