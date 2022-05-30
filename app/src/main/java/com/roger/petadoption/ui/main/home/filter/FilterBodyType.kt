package com.roger.petadoption.ui.main.home.filter

import com.roger.petadoption.R

enum class FilterBodyType(val content: String, val iconResId: Int) {
    SMALL("小", R.drawable.ic_body_type_small),
    MEDIUM("中", R.drawable.ic_body_type_medium),
    BIG("大", R.drawable.ic_body_type_big);

    companion object {
        fun getEnum(content: String): FilterBodyType = values().first() { content == it.content }
    }
}