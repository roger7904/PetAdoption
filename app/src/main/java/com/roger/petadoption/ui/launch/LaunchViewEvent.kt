package com.roger.petadoption.ui.launch

import com.roger.petadoption.ui.base.ViewEvent

sealed class LaunchViewEvent {
    object LoginRequired : ViewEvent.Data()
    object AuthPassed : ViewEvent.Data()
}