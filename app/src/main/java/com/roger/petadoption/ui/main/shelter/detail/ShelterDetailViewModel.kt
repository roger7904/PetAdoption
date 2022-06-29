package com.roger.petadoption.ui.main.shelter.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.entity.shelter.ShelterEntity
import com.roger.domain.use_case.shelter.GetShelterInfoUseCase
import com.roger.domain.use_case.weather.GetWeatherUseCase
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class ShelterDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getShelterInfoUseCase: GetShelterInfoUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) : BaseViewModel(state) {

    private val shelterId = state.getLiveData<String>(ShelterDetailActivity.ARG_SHELTER_ID)
    private val _shelterInfo = MutableLiveData<ShelterEntity>()
    val shelterInfo: LiveData<ShelterEntity> = _shelterInfo
    private val _shelterList = MutableLiveData<List<ShelterEntity>>()
    val shelterList: LiveData<List<ShelterEntity>> = _shelterList

    init {
        viewEventPublisher.onNext(ViewEvent.Loading)
        getShelterList()
    }

    private fun getShelterList() {
        val param = GetShelterInfoUseCase.Param(
            id = null
        )

        viewEventPublisher.onNext(ViewEvent.Loading)
        getShelterInfoUseCase(param).sub {
            _shelterList.postValue(it)
        }.addTo(compositeDisposable)
    }

    fun getShelterInfo() {
        val param = GetShelterInfoUseCase.Param(
            id = shelterId.value
        )

        getShelterInfoUseCase(param).sub {
            if (it?.isEmpty() == true) {
                viewEventPublisher.onNext(ShelterDetailViewEvent.ShelterInfoEmpty)
            } else {
                getWeather(it?.get(0))
            }
        }.addTo(compositeDisposable)
    }

    private fun getWeather(shelterEntity: ShelterEntity?) {
        val param = GetWeatherUseCase.Param(
            authorization = BuildConfig.WEATHER_API_KEY,
            locationName = shelterEntity?.cityName,
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
            _shelterInfo.postValue(
                ShelterEntity(
                    shelterName = shelterEntity?.shelterName,
                    phone = shelterEntity?.phone,
                    address = shelterEntity?.address,
                    lon = shelterEntity?.lon,
                    lat = shelterEntity?.lat,
                    weatherMin = min,
                    weatherMax = max,
                    weatherWx = wx,
                )
            )
        }.addTo(compositeDisposable)
    }
}