package com.roger.petadoption.ui.main.home.filter

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize


@Parcelize
enum class FilterType(val content: String, val iconResId: Int, val filter: String?) : Parcelable {
    CAT("貓貓", R.drawable.ic_cat, "貓"),
    DOG("狗狗", R.drawable.ic_dog, "狗"),
    CASUAL("不拘", R.drawable.ic_type_casual, null);

    companion object {
        fun getEnum(content: String): FilterType = values().first() { content == it.content }
    }
}