package com.roger.petadoption.ui.main.hospital.detail

import com.roger.petadoption.ui.base.ViewEvent

sealed class HospitalDetailViewEvent {
    object HospitalInfoEmpty : ViewEvent.Data()
}