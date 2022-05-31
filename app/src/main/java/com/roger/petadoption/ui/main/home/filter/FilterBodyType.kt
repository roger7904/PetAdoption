package com.roger.petadoption.ui.main.home.filter

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FilterBodyType(val content: String, val iconResId: Int, val filter: String?) :
    Parcelable {
    SMALL("小", R.drawable.ic_body_type_small, "SMALL"),
    MEDIUM("中", R.drawable.ic_body_type_medium, "MEDIUM"),
    BIG("大", R.drawable.ic_body_type_big, "BIG");

    companion object {
        fun getEnum(content: String): FilterBodyType = values().first() { content == it.content }
    }
}