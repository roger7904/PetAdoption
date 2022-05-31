package com.roger.petadoption.ui.main.home.filter

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FilterGender(val content: String, val iconResId: Int, val filter: String?) : Parcelable {
    MALE("男生", R.drawable.ic_male, "M"),
    FEMALE("女生", R.drawable.ic_female, "F"),
    CASUAL("不拘", R.drawable.ic_gender_casual, null);

    companion object {
        fun getEnum(content: String): FilterGender = values().first() { content == it.content }
    }
}