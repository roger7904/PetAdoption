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
import com.roger.petadoption.ui.main.home.filter.FilterActivity
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
    private val _type = state.getLiveData<String?>(FilterActivity.ARG_TYPE)
    val type: LiveData<String?> = _type
    private val _gender = state.getLiveData<String?>(FilterActivity.ARG_GENDER)
    val gender: LiveData<String?> = _gender
    private val _bodyType = state.getLiveData<String?>(FilterActivity.ARG_BODY_TYPE)
    val bodyType: LiveData<String?> = _bodyType
    private val _color = state.getLiveData<String?>(FilterActivity.ARG_COLOR)
    val color: LiveData<String?> = _color

    init {
        getFilterPagingList()
    }

    fun getFilterPagingList() {
        val param = GetPagingPetListUseCase.Param(
            animalKind = _type.value,
            animalSex = _gender.value,
            animalBodyType = _bodyType.value,
            animalColour = _color.value
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