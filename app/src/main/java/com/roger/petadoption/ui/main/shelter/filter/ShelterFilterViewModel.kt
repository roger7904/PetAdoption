package com.roger.petadoption.ui.main.shelter.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShelterFilterViewModel @Inject constructor(
    private val state: SavedStateHandle,
) : BaseViewModel(state) {

    private val _city = MutableLiveData<FilterCity?>()
    val city: LiveData<FilterCity?> = _city

    fun setCity(value: FilterCity?) {
        _city.value = value
    }
}