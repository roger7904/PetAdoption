package com.roger.petadoption.ui.auth

import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel(state) {

}