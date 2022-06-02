package com.roger.petadoption.ui.main.home

import com.roger.petadoption.ui.base.ViewEvent

sealed class PetDetailViewEvent {
    object SetFavoriteSuccess : ViewEvent.Data()
    object SetNotFavoriteSuccess : ViewEvent.Data()
}