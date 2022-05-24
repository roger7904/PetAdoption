package com.roger.petadoption.ui.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

open class BaseViewModel(private val state: SavedStateHandle) : ViewModel() {

    override fun onCleared() {
        super.onCleared()
    }
}


