package com.roger.petadoption.ui.main.home.filter

import com.roger.petadoption.R

enum class FilterColor(val content: String, val iconResId: Int) {
    COFFEE("咖啡色", R.drawable.ic_color_coffee),
    YELLOW("黃色", R.drawable.ic_color_yellow),
    TABBY("虎斑色", R.drawable.ic_color_tabby),
    BLACK("黑色", R.drawable.ic_color_black),
    BROWN("棕色", R.drawable.ic_color_brown),
    WHITE("白色", R.drawable.ic_color_white),
    TORTOISESHELL("玳瑁色", R.drawable.ic_color_tortoiseshell),
    BEIGE("米色", R.drawable.ic_color_beige),
    CASUAL("不拘", R.drawable.ic_color);

    companion object {
        fun getEnum(content: String): FilterColor = values().first() { content == it.content }
    }
}