package com.roger.petadoption.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import androidx.paging.filter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.common.DataResult
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.pet.GetFavoritePetListUseCase
import com.roger.domain.use_case.pet.GetPagingPetListUseCase
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.domain.use_case.pet.InsertFavoritePetUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import com.roger.petadoption.ui.main.home.filter.FilterBodyType
import com.roger.petadoption.ui.main.home.filter.FilterColor
import com.roger.petadoption.ui.main.home.filter.FilterGender
import com.roger.petadoption.ui.main.home.filter.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.kotlin.addTo
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getPagingPetListUseCase: GetPagingPetListUseCase,
    private val insertFavoritePetUseCase: InsertFavoritePetUseCase,
    private val getFavoritePetListUseCase: GetFavoritePetListUseCase,
) : BaseViewModel(state) {

    private var auth: FirebaseAuth = Firebase.auth
    private val _petListPagingData =
        MutableLiveData<PagingData<PetEntity>>()
    val petListPagingData: LiveData<PagingData<PetEntity>> =
        _petListPagingData
    private val _type = MutableLiveData<FilterType?>()
    val type: LiveData<FilterType?> = _type
    private val _gender = MutableLiveData<FilterGender?>()
    val gender: LiveData<FilterGender?> = _gender
    private val _bodyType = MutableLiveData<FilterBodyType?>()
    val bodyType: LiveData<FilterBodyType?> = _bodyType
    private val _color = MutableLiveData<FilterColor?>()
    val color: LiveData<FilterColor?> = _color
    private val _favoritePetList = MutableLiveData<MutableList<FavoritePetEntity>?>()
    val favoritePetList: LiveData<MutableList<FavoritePetEntity>?> = _favoritePetList

    init {
        getFavoritePetList()
    }

    fun setType(type: FilterType?) {
        _type.value = type
    }

    fun setGender(gender: FilterGender?) {
        _gender.value = gender
    }

    fun setBodyType(bodyType: FilterBodyType?) {
        _bodyType.value = bodyType
    }

    fun setColor(color: FilterColor?) {
        _color.value = color
    }

    fun getFilterPagingList() {
        val param = GetPagingPetListUseCase.Param(
            animalKind = _type.value?.filter,
            animalSex = _gender.value?.filter,
            animalBodyType = _bodyType.value?.filter,
            animalColour = _color.value?.filter
        )
        getPagingPetListUseCase(param).cache().subscribe { pagingData ->
            if (pagingData is DataResult.Success<PagingData<PetEntity>>) {
                pagingData.data?.let { list ->
                    val filterList =
                        list.filter { petEntity ->
                            !petEntity.albumFile.isNullOrEmpty()
                                    && !petEntity.variety.isNullOrEmpty()
                                    && !petEntity.petPlace.isNullOrEmpty()
                                    && _favoritePetList.value?.map { it.petId }
                                ?.contains(petEntity.id) != true
                        }
                    _petListPagingData.postValue(filterList)
                }
            }
        }
    }

    fun insertFavoritePet(petId: Int) {
        val param = InsertFavoritePetUseCase.Param(
            id = System.currentTimeMillis().toString(),
            userId = auth.currentUser?.uid ?: "",
            petId = petId
        )

        _favoritePetList.value?.add(
            FavoritePetEntity(
                id = param.id,
                userId = param.userId,
                petId = param.petId
            )
        )

        insertFavoritePetUseCase(param).sub {
        }.addTo(compositeDisposable)
    }

    fun getFavoritePetList() {
        val param = GetFavoritePetListUseCase.Param(
            userId = auth.currentUser?.uid ?: ""
        )

        getFavoritePetListUseCase(param).sub {
            _favoritePetList.postValue(it?.toMutableList())
            getFilterPagingList()
        }.addTo(compositeDisposable)
    }
}