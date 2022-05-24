package com.roger.petadoption.ui.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase
) : BaseViewModel(state) {

    init {
        Log.d("TAG", "init: ")
        viewEventPublisher.onNext(ViewEvent.Loading)
        getPetInfoUseCase().sub(onError = {
            Log.d("TAG", ": $it")
            true
        }) {
            Log.d("TAG", ": $it")
            viewEventPublisher.onNext(ViewEvent.Done)
        }.addTo(compositeDisposable)
    }
}