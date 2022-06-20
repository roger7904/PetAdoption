package com.roger.petadoption.ui.main.hospital.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.domain.use_case.weather.GetWeatherUseCase
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class HospitalDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getHospitalInfoUseCase: GetHospitalInfoUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) : BaseViewModel(state) {

    private val hospitalId = state.getLiveData<String>(HospitalDetailActivity.ARG_HOSPITAL_ID)
    private val _hospitalInfo = MutableLiveData<HospitalEntity>()
    val hospitalInfo: LiveData<HospitalEntity> = _hospitalInfo
    private val _hospitalList =
        state.getLiveData<List<HospitalEntity>>(HospitalDetailActivity.ARG_HOSPITAL_LIST)
    val hospitalList: LiveData<List<HospitalEntity>> = _hospitalList

    init {
        viewEventPublisher.onNext(ViewEvent.Loading)
    }

    fun getHospitalInfo() {
        val param = GetHospitalInfoUseCase.Param(
            top = null,
            skip = null,
            filter = "字號+like+${hospitalId.value}"
        )

        getHospitalInfoUseCase(param).sub {
            getWeather(it?.get(0))
        }.addTo(compositeDisposable)
    }

    private fun getWeather(hospitalEntity: HospitalEntity?) {
        val param = GetWeatherUseCase.Param(
            authorization = BuildConfig.WEATHER_API_KEY,
            locationName = hospitalEntity?.city,
            sort = "time",
        )

        var min: String? = null
        var max: String? = null
        var wx: String? = null

        getWeatherUseCase(param).sub {
            it?.records?.location?.get(0)?.weatherElement?.forEach { element ->
                when (element.elementName) {
                    "MinT" -> {
                        min = element.time?.get(0)?.parameter?.parameterName!!
                    }
                    "MaxT" -> {
                        max = element.time?.get(0)?.parameter?.parameterName!!
                    }
                    "Wx" -> {
                        wx = element.time?.get(0)?.parameter?.parameterValue!!
                    }
                }
            }
            _hospitalInfo.postValue(
                HospitalEntity(
                    name = hospitalEntity?.name,
                    mobile = hospitalEntity?.mobile,
                    location = hospitalEntity?.location,
                    weatherMin = min,
                    weatherMax = max,
                    weatherWx = wx,
                )
            )
        }.addTo(compositeDisposable)
    }
}