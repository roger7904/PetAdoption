package com.roger.petadoption.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.DeleteFavoritePetUseCase
import com.roger.domain.use_case.pet.GetFavoritePetListUseCase
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.domain.use_case.pet.InsertFavoritePetUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.base.ViewEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class PetDetailViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPetInfoUseCase: GetPetInfoUseCase,
    private val getFavoritePetListUseCase: GetFavoritePetListUseCase,
    private val insertFavoritePetUseCase: InsertFavoritePetUseCase,
    private val deleteFavoritePetUseCase: DeleteFavoritePetUseCase,
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth
    private val petId = state.getLiveData<Int>(PetDetailActivity.ARG_PET_ID)
    private val _petInfo = MutableLiveData<PetEntity>()
    val petInfo: LiveData<PetEntity> = _petInfo
    private val _isFavorite = MutableLiveData<Boolean?>()
    val isFavorite: LiveData<Boolean?> = _isFavorite
    private val _favoritePetList = MutableLiveData<MutableList<FavoritePetEntity>?>()
    val favoritePetList: LiveData<MutableList<FavoritePetEntity>?> = _favoritePetList

    init {
        viewEventPublisher.onNext(ViewEvent.Loading)
        getFavoritePetList()
    }

    fun setFavorite() {
        if (_isFavorite.value == true) {
            val param = DeleteFavoritePetUseCase.Param(
                userId = auth.currentUser?.uid ?: "",
                petId = _petInfo.value?.id ?: 0
            )

            deleteFavoritePetUseCase(param).sub {
                _isFavorite.postValue(false)
                viewEventPublisher.onNext(PetDetailViewEvent.SetNotFavoriteSuccess)
            }.addTo(compositeDisposable)
        } else {
            val param = InsertFavoritePetUseCase.Param(
                id = System.currentTimeMillis().toString(),
                userId = auth.currentUser?.uid ?: "",
                petId = _petInfo.value?.id ?: 0
            )

            insertFavoritePetUseCase(param).sub {
                _isFavorite.postValue(true)
                viewEventPublisher.onNext(PetDetailViewEvent.SetFavoriteSuccess)
            }.addTo(compositeDisposable)
        }
    }

    private fun getPetInfo() {
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
            _petInfo.postValue(petEntity?.get(0))
            _isFavorite.postValue(
                _favoritePetList.value?.map { it.petId }?.contains(petEntity?.get(0)?.id)
            )
        }.addTo(compositeDisposable)
    }


    private fun getFavoritePetList() {
        val param = GetFavoritePetListUseCase.Param(
            userId = auth.currentUser?.uid ?: ""
        )

        getFavoritePetListUseCase(param).sub {
            _favoritePetList.postValue(it?.toMutableList())
            getPetInfo()
        }.addTo(compositeDisposable)
    }
}