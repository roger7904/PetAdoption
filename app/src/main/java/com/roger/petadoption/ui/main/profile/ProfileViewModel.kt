package com.roger.petadoption.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.use_case.user.GetUserUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getUserUseCase: GetUserUseCase,
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        val param = GetUserUseCase.Param(
            auth.currentUser?.uid ?: ""
        )
        getUserUseCase(param).sub {
            _userName.postValue(it?.userName ?: auth.currentUser?.displayName)
        }.addTo(compositeDisposable)
    }

    fun setUserName(value: String) {
        _userName.postValue(value)
    }

    fun signOut() {
        auth.signOut()
        viewEventPublisher.onNext(ProfileViewEvent.SignOut)
    }
}