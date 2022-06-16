package com.roger.petadoption.ui.main.home.detail

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class WeatherType(val id: String, val iconResId: Int) : Parcelable {
    WX1("1", R.drawable.ic_sun),
    WX2("2", R.drawable.ic_sun_cloudy),
    WX3("3", R.drawable.ic_sun_cloudy),
    WX4("4", R.drawable.ic_cloudy),
    WX5("5", R.drawable.ic_cloudy),
    WX6("6", R.drawable.ic_cloudy),
    WX7("7", R.drawable.ic_cloudy),
    WX8("8", R.drawable.ic_rainy),
    WX9("9", R.drawable.ic_rainy),
    WX10("10", R.drawable.ic_rainy),
    WX11("11", R.drawable.ic_sun_rainy),
    WX12("12", R.drawable.ic_rainy),
    WX13("13", R.drawable.ic_rainy),
    WX14("14", R.drawable.ic_rainy),
    WX15("15", R.drawable.ic_thunder),
    WX16("16", R.drawable.ic_thunder),
    WX17("17", R.drawable.ic_thunder),
    WX18("18", R.drawable.ic_thunder),
    WX19("19", R.drawable.ic_sun_rainy),
    WX20("20", R.drawable.ic_rainy),
    WX21("21", R.drawable.ic_thunder),
    WX22("22", R.drawable.ic_thunder),
    WX23("23", R.drawable.ic_rainy),
    WX24("24", R.drawable.ic_cloudy),
    WX25("25", R.drawable.ic_cloudy),
    WX26("26", R.drawable.ic_cloudy),
    WX27("27", R.drawable.ic_cloudy),
    WX28("28", R.drawable.ic_cloudy),
    WX29("29", R.drawable.ic_rainy),
    WX30("30", R.drawable.ic_rainy),
    WX31("31", R.drawable.ic_rainy),
    WX32("32", R.drawable.ic_rainy),
    WX33("33", R.drawable.ic_thunder),
    WX34("34", R.drawable.ic_thunder),
    WX35("35", R.drawable.ic_rainy),
    WX36("36", R.drawable.ic_thunder),
    WX37("37", R.drawable.ic_rainy),
    WX38("38", R.drawable.ic_rainy),
    WX39("39", R.drawable.ic_rainy),
    WX40("40", R.drawable.ic_rainy),
    WX41("41", R.drawable.ic_thunder),
    WX42("42", R.drawable.ic_snowy);

    companion object {
        fun getEnum(id: String): WeatherType = values().first() { id == it.id }
    }
}