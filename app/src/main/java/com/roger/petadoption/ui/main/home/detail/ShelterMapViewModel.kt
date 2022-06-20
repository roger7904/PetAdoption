package com.roger.petadoption.ui.main.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.data.mapper.pet.PetWeatherMapper
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.domain.use_case.weather.GetWeatherUseCase
import com.roger.petadoption.BuildConfig
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class ShelterMapViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
    private val petWeatherMapper: PetWeatherMapper,
) : BaseViewModel(state) {

    private val petId = state.getLiveData<Int>(ShelterMapActivity.ARG_PET_MAP_ID)
    private val _petInfo = MutableLiveData<PetEntity>()
    val petInfo: LiveData<PetEntity> = _petInfo

    init {
        viewEventPublisher.onNext(ViewEvent.Loading)
    }

    fun getPetInfo() {
        val param = GetPetInfoUseCase.Param(
            animalId = petId.value,
            top = null,
            skip = null,
            animalKind = null,
            animalSex = null,
            animalBodyType = null,
            animalColour = null
        )

        getPetInfoUseCase(param).sub { petEntity ->
            getWeather(petEntity?.get(0))
        }.addTo(compositeDisposable)
    }

    private fun getWeather(petEntity: PetEntity?) {
        val param = GetWeatherUseCase.Param(
            authorization = BuildConfig.WEATHER_API_KEY,
            locationName = petEntity?.shelterAddress?.substring(0, 3),
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
            _petInfo.postValue(petWeatherMapper.toWeatherEntity(petEntity, min, max, wx))
        }.addTo(compositeDisposable)
    }
}