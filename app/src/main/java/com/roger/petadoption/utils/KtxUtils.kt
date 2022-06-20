package com.roger.petadoption.utils

import android.content.Context
import android.util.TypedValue
import java.util.regex.Pattern

fun Int.toPx(context: Context): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    )
}

fun String?.isPasswordValid(): Boolean {
    this ?: return false
    val regex = "(?=.*[A-Z,a-z])(?=.*\\d).{8,20}"
    val pattern = Pattern.compile(regex)
    val matcher = pattern.matcher(this)
    return matcher.matches()
}
