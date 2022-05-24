package com.roger.domain.repository.pet

import com.roger.domain.entity.pet.PetEntity
import io.reactivex.rxjava3.core.Single

interface PetRepository {
    fun getPetInfo(): Single<List<PetEntity>>
}