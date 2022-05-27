package com.roger.petadoption.ui.main

import androidx.lifecycle.SavedStateHandle
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase
) : BaseViewModel(state) {

    init {
//        Log.d("TAG", "init: ")
//        viewEventPublisher.onNext(ViewEvent.Loading)
//        getPetInfoUseCase().sub(onError = {
//            Log.d("getPetInfoUseCase", ": $it")
//            true
//        }) {
//            Log.d("TAG", "BaseViewModel: $it")
//            viewEventPublisher.onNext(ViewEvent.Done)
//        }.addTo(compositeDisposable)
    }
}