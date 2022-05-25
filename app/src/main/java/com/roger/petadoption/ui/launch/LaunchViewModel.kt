package com.roger.petadoption.ui.launch

import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth

    fun checkLaunchMode() {
        if (auth.currentUser != null) {
            viewEventPublisher.onNext(LaunchViewEvent.AuthPassed)
        }else{
            viewEventPublisher.onNext(LaunchViewEvent.LoginRequired)
        }
    }
}