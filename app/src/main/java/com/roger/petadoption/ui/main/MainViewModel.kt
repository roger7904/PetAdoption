package com.roger.petadoption.ui.main

import android.content.Context
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roger.domain.common.DataResult
import com.roger.domain.entity.hospital.HospitalEntity
import com.roger.domain.entity.pet.FavoritePetEntity
import com.roger.domain.entity.pet.PetEntity
import com.roger.domain.use_case.hospital.GetHospitalInfoUseCase
import com.roger.domain.use_case.pet.DeleteFavoritePetUseCase
import com.roger.domain.use_case.pet.GetFavoritePetListUseCase
import com.roger.domain.use_case.pet.GetPetInfoUseCase
import com.roger.petadoption.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val getFavoritePetListUseCase: GetFavoritePetListUseCase,
    private val getPetInfoUseCase: GetPetInfoUseCase,
    private val deleteFavoritePetUseCase: DeleteFavoritePetUseCase,
    private val getHospitalInfoUseCase: GetHospitalInfoUseCase,
) : BaseViewModel(state) {
    private var auth: FirebaseAuth = Firebase.auth
    private val _favoritePetList = MutableLiveData<MutableList<FavoritePetEntity>?>()
    private val _isNeedRefresh = MutableLiveData<Boolean?>()
    private val _favoritePetInfoList = MutableLiveData<MutableList<PetEntity>?>()
    val favoritePetInfoList: LiveData<MutableList<PetEntity>?> = _favoritePetInfoList
    private val _hospitalLocationList = MutableLiveData<ArrayList<HospitalEntity>>()
    val hospitalLocationList: LiveData<ArrayList<HospitalEntity>> = _hospitalLocationList

    init {
        getFavoritePetList()
    }

    fun setIsNeedRefresh(value: Boolean) {
        _isNeedRefresh.value = value
    }

    fun getFavoritePetList() {
        if (_isNeedRefresh.value == false) return
        val param = GetFavoritePetListUseCase.Param(
            userId = auth.currentUser?.uid ?: ""
        )

        getFavoritePetListUseCase(param).sub {
            _favoritePetList.postValue(it?.toMutableList())
            getFavoritePetInfo(it ?: emptyList())
        }.addTo(compositeDisposable)

    }

    private fun getFavoritePetInfo(favoriteList: List<FavoritePetEntity>) {
        var emptyCount = 0
        _favoritePetInfoList.postValue(mutableListOf())
        val itr = favoriteList.listIterator()
        while (itr.hasNext()) {
            val param = GetPetInfoUseCase.Param(
                animalId = itr.next().petId,
                top = null,
                skip = null,
                animalKind = null,
                animalSex = null,
                animalBodyType = null,
                animalColour = null
            )

            getPetInfoUseCase(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { result, _ ->
                    if (result is DataResult.Success<List<PetEntity>>) {
                        if (result.data?.isNullOrEmpty() == true) {
                            val paramDelete = DeleteFavoritePetUseCase.Param(
                                userId = auth.currentUser?.uid ?: "",
                                petId = param.animalId ?: 0
                            )
                            emptyCount += 1
                            if (_favoritePetInfoList.value?.size ?: 0 >= favoriteList.size - emptyCount) {
                                _favoritePetInfoList.value?.sortBy { it.id }
                                _favoritePetInfoList.postValue(_favoritePetInfoList.value)
                                setIsNeedRefresh(false)
                            }
                            deleteFavoritePetUseCase(paramDelete).sub {

                            }.addTo(compositeDisposable)
                            return@subscribe
                        }
                        result.data?.get(0)?.let { petEntity ->
                            _favoritePetInfoList.value?.add(
                                petEntity
                            )
                            if (_favoritePetInfoList.value?.size ?: 0 >= favoriteList.size - emptyCount) {
                                _favoritePetInfoList.value?.sortBy { it.id }
                                _favoritePetInfoList.postValue(_favoritePetInfoList.value)
                                setIsNeedRefresh(false)
                            }
                        }
                    }
                }.addTo(compositeDisposable)
        }
    }

    fun removeFavorite(petId: Int) {
        val param = DeleteFavoritePetUseCase.Param(
            userId = auth.currentUser?.uid ?: "",
            petId = petId
        )
        _favoritePetInfoList.value?.removeAll { it.id == petId }
        _favoritePetInfoList.value = _favoritePetInfoList.value

        deleteFavoritePetUseCase(param).sub {

        }.addTo(compositeDisposable)
    }

    fun getHospitalList(context: Context) {
        val param = GetHospitalInfoUseCase.Param(
            top = 100,
            skip = 0,
            filter = null
        )

        getHospitalInfoUseCase(param).sub { hospitalList ->
            _hospitalLocationList.postValue(
                getLocationFromAddress(context, hospitalList)
            )
        }.addTo(compositeDisposable)
    }

    private fun getLocationFromAddress(
        context: Context,
        hospitalList: List<HospitalEntity>?,
    ): ArrayList<HospitalEntity> {
        val coder = Geocoder(context)
        val hospitalEntityList: MutableList<HospitalEntity> = mutableListOf()
        try {
            hospitalList?.forEach { hospitalEntity ->
                val address =
                    coder.getFromLocationName(hospitalEntity.location, 5) ?: return@forEach
                val location: Address = address[0]
                hospitalEntityList.add(
                    HospitalEntity(
                        name = hospitalEntity.name,
                        mobile = hospitalEntity.mobile,
                        location = hospitalEntity.location,
                        lat = location.latitude,
                        lng = location.longitude,
                    )
                )
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        return ArrayList(hospitalEntityList)
    }
}