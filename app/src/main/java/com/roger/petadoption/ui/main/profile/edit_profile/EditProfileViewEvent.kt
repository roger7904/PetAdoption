package com.roger.petadoption.ui.main.profile.edit_profile

import com.roger.petadoption.ui.base.ViewEvent

sealed class EditProfileViewEvent {
    data class UpdateUserSuccess(val useName: String) : ViewEvent.Data()
}