package com.roger.petadoption.ui.main.hospital

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.roger.domain.common.DataResult
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetPagingHospitalListUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPagingHospitalListUseCase: GetPagingHospitalListUseCase,
) : BaseViewModel(state) {

    private val _region = MutableLiveData<FilterRegion?>()
    val region: LiveData<FilterRegion?> = _region
    private val _hospitalListPagingData =
        MutableLiveData<PagingData<HospitalEntity>>()
    val hospitalListPagingData: LiveData<PagingData<HospitalEntity>> =
        _hospitalListPagingData

    init {
        getFilterPagingList()
    }

    fun setRegion(type: FilterRegion?) {
        _region.value = type
    }

    fun getFilterPagingList() {
        val param = GetPagingHospitalListUseCase.Param(
            filter = _region.value?.filter,
        )

        getPagingHospitalListUseCase(param).cache().subscribe { pagingData ->
            if (pagingData is DataResult.Success<PagingData<HospitalEntity>>) {
                pagingData.data?.let { list ->
                    _hospitalListPagingData.postValue(list)
                }
            }
        }
    }
}