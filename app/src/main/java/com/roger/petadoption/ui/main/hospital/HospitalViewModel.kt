package com.roger.petadoption.ui.main.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val state: SavedStateHandle
    ) : BaseViewModel(state) {

    private val _region = MutableLiveData<FilterRegion?>()
    val region: LiveData<FilterRegion?> = _region

    fun setRegion(type: FilterRegion?) {
        _region.value = type
    }
}