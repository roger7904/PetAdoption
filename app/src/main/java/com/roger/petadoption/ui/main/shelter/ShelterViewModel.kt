package com.roger.petadoption.ui.main.shelter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.roger.domain.common.DataResult
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.use_case.shelter.GetPagingShelterListUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.shelter.filter.FilterCity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShelterViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPagingShelterListUseCase: GetPagingShelterListUseCase,
) : BaseViewModel(state) {

    private val _shelterListPagingData =
        MutableLiveData<PagingData<ShelterEntity>>()
    val shelterListPagingData: LiveData<PagingData<ShelterEntity>> =
        _shelterListPagingData
    private val _city = MutableLiveData<FilterCity?>()
    val city: LiveData<FilterCity?> = _city

    init {
        getFilterPagingList()
    }

    fun setCity(value: FilterCity?) {
        _city.value = value
    }

    fun getFilterPagingList() {
        val param = GetPagingShelterListUseCase.Param(
            cityName = _city.value?.content,
        )

        getPagingShelterListUseCase(param).cache().subscribe { pagingData ->
            if (pagingData is DataResult.Success<PagingData<ShelterEntity>>) {
                pagingData.data?.let { list ->
                    _shelterListPagingData.postValue(list)
                }
            }
        }
    }
}