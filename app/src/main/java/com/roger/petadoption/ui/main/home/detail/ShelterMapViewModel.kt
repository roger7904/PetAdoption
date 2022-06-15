package com.roger.petadoption.ui.main.home.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class PetMapViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase,
) : BaseViewModel(state) {

    private val petId = state.getLiveData<Int>(ShelterMapActivity.ARG_PET_MAP_ID)
    private val _petInfo = MutableLiveData<PetEntity>()
    val petInfo: LiveData<PetEntity> = _petInfo

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

        viewEventPublisher.onNext(ViewEvent.Loading)
        getPetInfoUseCase(param).sub { petEntity ->
            _petInfo.postValue(petEntity?.get(0))
        }.addTo(compositeDisposable)
    }
}