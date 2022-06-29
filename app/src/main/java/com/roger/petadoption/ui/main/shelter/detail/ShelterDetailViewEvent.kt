package com.roger.petadoption.ui.main.shelter.detail

import com.roger.petadoption.ui.base.ViewEvent

sealed class ShelterDetailViewEvent {
    object ShelterInfoEmpty : ViewEvent.Data()
}