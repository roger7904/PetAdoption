package com.roger.petadoption.ui.main.home.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val state: SavedStateHandle,
) : BaseViewModel(state) {

    private val _type = MutableLiveData<FilterType?>()
    val type: LiveData<FilterType?> = _type
    private val _gender = MutableLiveData<FilterGender?>()
    val gender: LiveData<FilterGender?> = _gender
    private val _bodyType = MutableLiveData<FilterBodyType?>()
    val bodyType: LiveData<FilterBodyType?> = _bodyType
    private val _color = MutableLiveData<FilterColor?>()
    val color: LiveData<FilterColor?> = _color

    fun setType(type: FilterType?) {
        _type.value = type
    }

    fun setGender(gender: FilterGender?) {
        _gender.value = gender
    }

    fun setBodyType(bodyType: FilterBodyType?) {
        _bodyType.value = bodyType
    }

    fun setColor(color: FilterColor?) {
        _color.value = color
    }
}