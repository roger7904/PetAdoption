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

    init {
        val param = GetPagingPetListUseCase.Param(
            animalKind = null,
            animalSex = null
        )
        getPagingPetListUseCase(param).cache().subscribe { pagingData ->
            if (pagingData is DataResult.Success<PagingData<PetEntity>>) {
                pagingData.data?.let { list ->
                    val filterList = list.filter { !it.albumFile.isNullOrEmpty() && !it.variety.isNullOrEmpty() && !it.petPlace.isNullOrEmpty() }
                    _petListPagingData.postValue(filterList)
                }
            }
        }
    }
}