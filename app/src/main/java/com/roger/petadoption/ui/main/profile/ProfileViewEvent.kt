package com.roger.petadoption.ui.main.profile

import com.roger.petadoption.ui.base.ViewEvent

sealed class ProfileViewEvent {
    object SignOut : ViewEvent.Data()
}