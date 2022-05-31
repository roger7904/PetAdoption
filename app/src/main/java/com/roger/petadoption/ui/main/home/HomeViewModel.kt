package com.roger.petadoption.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.filter
import com.roger.domain.common.DataResult
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPagingPetListUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.home.filter.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPagingPetListUseCase: GetPagingPetListUseCase,
) : BaseViewModel(state) {
    private val _petListPagingData =
        MutableLiveData<PagingData<PetEntity>>()
    val petListPagingData: LiveData<PagingData<PetEntity>> =
        _petListPagingData
    private val _type = MutableLiveData<FilterType?>()
    val type: LiveData<FilterType?> = _type
    private val _gender = MutableLiveData<FilterGender?>()
    val gender: LiveData<FilterGender?> = _gender
    private val _bodyType = MutableLiveData<FilterBodyType?>()
    val bodyType: LiveData<FilterBodyType?> = _bodyType
    private val _color = MutableLiveData<FilterColor?>()
    val color: LiveData<FilterColor?> = _color

    init {
        getFilterPagingList()
    }

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

    fun getFilterPagingList() {
        val param = GetPagingPetListUseCase.Param(
            animalKind = _type.value?.filter,
            animalSex = _gender.value?.filter,
            animalBodyType = _bodyType.value?.filter,
            animalColour = _color.value?.filter
        )
        getPagingPetListUseCase(param).cache().subscribe { pagingData ->
            if (pagingData is DataResult.Success<PagingData<PetEntity>>) {
                pagingData.data?.let { list ->
                    val filterList =
                        list.filter { !it.albumFile.isNullOrEmpty() && !it.variety.isNullOrEmpty() && !it.petPlace.isNullOrEmpty() }
                    _petListPagingData.postValue(filterList)
                }
            }
        }
    }
}