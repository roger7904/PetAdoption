package com.roger.petadoption.ui.auth

import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val state: SavedStateHandle
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth

    fun firebaseLogin(credential: AuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    viewEventPublisher.onNext(SignInViewEvent.LoginSuccess)
                } else {
                    viewEventPublisher.onNext(SignInViewEvent.LoginFail)
                }
            }
    }
}