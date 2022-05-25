package com.roger.petadoption.ui.main.map

import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val state: SavedStateHandle) :
    BaseViewModel(state) {
}