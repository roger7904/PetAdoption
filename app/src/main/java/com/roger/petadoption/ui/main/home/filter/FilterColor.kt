package com.roger.petadoption.ui.main.home.filter

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FilterColor(val content: String, val iconResId: Int, val filter: String?) : Parcelable {
    COFFEE("咖啡色", R.drawable.ic_color_coffee, "咖啡色"),
    YELLOW("黃色", R.drawable.ic_color_yellow, "黃色"),
    TABBY("虎斑色", R.drawable.ic_color_tabby, "虎斑色"),
    BLACK("黑色", R.drawable.ic_color_black, "黑色"),
    BROWN("棕色", R.drawable.ic_color_brown, "棕色"),
    WHITE("白色", R.drawable.ic_color_white, "白色"),
    TORTOISESHELL("玳瑁色", R.drawable.ic_color_tortoiseshell, "玳瑁色"),
    BEIGE("米色", R.drawable.ic_color_beige, "米色"),
    CASUAL("不拘", R.drawable.ic_color, null);

    companion object {
        fun getEnum(content: String): FilterColor = values().first() { content == it.content }
    }
}