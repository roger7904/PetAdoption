package com.roger.petadoption.ui.main.profile.edit_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.use_case.user.GetUserUseCase
import com.roger.domain.use_case.user.UpdateUserUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth

    private val _userEmail = MutableLiveData<String>()
    val userEmail: LiveData<String>
        get() = _userEmail

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String>
        get() = _userName

    init {
        _userEmail.postValue(auth.currentUser?.email)

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

    fun updateUser() {
        val param = UpdateUserUseCase.Param(
            auth.currentUser?.uid ?: "",
            _userName.value ?: ""
        )
        updateUserUseCase(param).sub {
            viewEventPublisher.onNext(EditProfileViewEvent.UpdateUserSuccess(_userName.value ?: ""))
        }.addTo(compositeDisposable)
    }
}