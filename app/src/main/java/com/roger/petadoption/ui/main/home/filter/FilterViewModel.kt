package com.roger.petadoption.ui.main.home.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val state: SavedStateHandle,
) : BaseViewModel(state) {

    private val _type = state.getLiveData<String?>(FilterActivity.ARG_TYPE)
    val type: LiveData<String?> = _type
    private val _gender = state.getLiveData<String?>(FilterActivity.ARG_GENDER)
    val gender: LiveData<String?> = _gender
    private val _bodyType = state.getLiveData<String?>(FilterActivity.ARG_BODY_TYPE)
    val bodyType: LiveData<String?> = _bodyType
    private val _color = state.getLiveData<String?>(FilterActivity.ARG_COLOR)
    val color: LiveData<String?> = _color


}