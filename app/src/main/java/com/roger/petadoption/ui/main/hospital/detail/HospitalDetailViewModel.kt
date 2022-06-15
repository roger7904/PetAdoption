package com.roger.petadoption.ui.main.hospital.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class HospitalDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getHospitalInfoUseCase: GetHospitalInfoUseCase,
) : BaseViewModel(state) {

    private val hospitalId = state.getLiveData<String>(HospitalDetailActivity.ARG_HOSPITAL_ID)
    private val _hospitalInfo = MutableLiveData<List<HospitalEntity>>()
    val hospitalInfo: LiveData<List<HospitalEntity>> = _hospitalInfo
    private val _hospitalLocationList =
        state.getLiveData<List<HospitalLocationEntity>>(HospitalDetailActivity.ARG_HOSPITAL_LIST)
    val hospitalLocationList: LiveData<List<HospitalLocationEntity>> = _hospitalLocationList

    fun getHospitalInfo() {
        val param = GetHospitalInfoUseCase.Param(
            top = null,
            skip = null,
            filter = "字號+like+${hospitalId.value}"
        )

        viewEventPublisher.onNext(ViewEvent.Loading)
        getHospitalInfoUseCase(param).sub {
            _hospitalInfo.postValue(it)
        }.addTo(compositeDisposable)
    }
}