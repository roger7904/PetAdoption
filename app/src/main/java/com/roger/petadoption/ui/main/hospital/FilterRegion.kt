package com.roger.petadoption.ui.main.hospital

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize


@Parcelize
enum class FilterRegion(val content: String, val filter: String) : Parcelable {
    NORTH("北部",
        "縣市+like+基隆市+or+縣市+like+臺北市+or+縣市+like+新北市+or+縣市+like+桃園市+or+縣市+like+新竹市+or+縣市+like+新竹縣+or+縣市+like+宜蘭縣"),
    CENTRAL("中部",
        "縣市+like+苗栗縣+or+縣市+like+臺中市+or+縣市+like+彰化縣+or+縣市+like+南投縣+or+縣市+like+雲林縣"),
    SOUTH("南部",
        "縣市+like+嘉義市+or+縣市+like+嘉義縣+or+縣市+like+臺南市+or+縣市+like+高雄市+or+縣市+like+屏東縣"),
    EAST("東部", "縣市+like+花蓮縣+or+縣市+like+臺東縣"),
    ISLAND("離島", "縣市+like+澎湖縣+or+縣市+like+金門縣+or+縣市+like+連江縣");

    companion object {
        fun getEnum(content: String): FilterRegion = values().first() { content == it.content }
    }
}