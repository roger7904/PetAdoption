package com.roger.petadoption.ui.main.home.filter

import com.roger.petadoption.R

enum class FilterType(val content: String, val iconResId: Int) {
    CAT("貓貓", R.drawable.ic_cat),
    DOG("狗狗", R.drawable.ic_dog),
    CASUAL("不拘", R.drawable.ic_type_casual);

    companion object {
        fun getEnum(content: String): FilterType = values().first() { content == it.content }
    }
}