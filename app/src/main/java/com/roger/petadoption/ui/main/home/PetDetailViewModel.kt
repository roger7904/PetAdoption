package com.roger.petadoption.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase,
) : BaseViewModel(state) {

    private val petId = state.getLiveData<Int>(PetDetailActivity.ARG_PET_ID)
    private val _petInfo = MutableLiveData<PetEntity>()
    val petInfo: LiveData<PetEntity> = _petInfo

    init {
        val param = GetPetInfoUseCase.Param(
            animalId = petId.value,
            top = null,
            skip = null,
            animalKind = null,
            animalSex = null
        )

        getPetInfoUseCase(param).sub {
            _petInfo.postValue(it?.get(0))
        }.addTo(compositeDisposable)
    }
}