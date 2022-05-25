package com.roger.petadoption.ui.launch

import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LaunchViewModel @Inject constructor(private val state: SavedStateHandle) :
    BaseViewModel(state) {}