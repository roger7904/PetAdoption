package com.roger.petadoption.ui.main.home.filter

import com.roger.petadoption.R

enum class FilterGender(val content: String, val iconResId: Int) {
    MALE("男生", R.drawable.ic_male),
    FEMALE("女生", R.drawable.ic_female),
    CASUAL("不拘", R.drawable.ic_gender_casual);

    companion object {
        fun getEnum(content: String): FilterGender = values().first() { content == it.content }
    }
}