package com.roger.petadoption.ui.auth

import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.use_case.user.InitUserUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val initUserUseCase: InitUserUseCase,
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth

    fun firebaseLogin(credential: AuthCredential) {
        viewEventPublisher.onNext(ViewEvent.Loading)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val param = InitUserUseCase.Param(
                        auth.currentUser?.uid ?: "",
                        auth.currentUser?.displayName ?: "User"
                    )
                    initUserUseCase(param).sub {
                        viewEventPublisher.onNext(SignInViewEvent.LoginSuccess)
                    }.addTo(compositeDisposable)
                } else {
                    viewEventPublisher.onNext(ViewEvent.Done)
                    viewEventPublisher.onNext(SignInViewEvent.LoginFail)
                }
            }
    }

    fun passwordSignIn(account: String?, password: String?) {
        viewEventPublisher.onNext(ViewEvent.Loading)
        if (account.isNullOrEmpty() || password.isNullOrEmpty()) {
            viewEventPublisher.onNext(ViewEvent.Done)
            viewEventPublisher.onNext(SignInViewEvent.PasswordLoginFail)
            return
        }
        auth.signInWithEmailAndPassword(account, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val param = InitUserUseCase.Param(
                        auth.currentUser?.uid ?: "",
                        auth.currentUser?.displayName ?: "User"
                    )
                    initUserUseCase(param).sub {
                        viewEventPublisher.onNext(SignInViewEvent.LoginSuccess)
                    }.addTo(compositeDisposable)
                } else {
                    viewEventPublisher.onNext(ViewEvent.Done)
                    viewEventPublisher.onNext(SignInViewEvent.PasswordLoginFail)
                }
            }
    }
}