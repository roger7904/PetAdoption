package com.roger.domain.entity.pet

import java.io.Serializable

data class FavoritePetEntity(
    val id: String? = null,
    val userId: String? = null,
    val petId: Int? = null,
) : Serializable