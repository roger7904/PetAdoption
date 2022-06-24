package com.roger.petadoption.ui.main.shelter.filter

import android.os.Parcelable
import com.roger.petadoption.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class FilterCity(val iconResId: Int, val content: String) :
    Parcelable {
    KeelungCity(R.drawable.ic_keelung, "基隆市"),
    TaipeiCity(R.drawable.ic_taipei_city, "臺北市"),
    NewTaipeiCity(R.drawable.ic_new_taipei_city, "新北市"),
    TaoyuanCity(R.drawable.ic_taoyuan_city, "桃園市"),
    HsinchuCity(R.drawable.ic_hsinchu_city, "新竹市"),
    HsinchuCounty(R.drawable.ic_hsinchu_county, "新竹縣"),
    YilanCounty(R.drawable.ic_yilan_county, "宜蘭縣"),

    MiaoliCounty(R.drawable.ic_miaoli_county, "苗栗縣"),
    TaichungCity(R.drawable.ic_taichung, "臺中市"),
    ChanghuaCounty(R.drawable.ic_changhua_county, "彰化縣"),
    NantouCounty(R.drawable.ic_nantou_county, "南投縣"),
    YunlinCounty(R.drawable.ic_yunlin_county, "雲林縣"),

    ChiayiCity(R.drawable.ic_chiayi_city, "嘉義市"),
    ChiayiCounty(R.drawable.ic_chiayi_county, "嘉義縣"),
    TainanCity(R.drawable.ic_tainan_city, "臺南市"),
    KaohsiungCity(R.drawable.ic_kaohsiung_city, "高雄市"),
    PingtungCounty(R.drawable.ic_pingtung_county, "屏東縣"),

    HualienCounty(R.drawable.ic_hualien_county, "花蓮縣"),
    TaitungCounty(R.drawable.ic_taitung_county, "臺東縣"),

    PenghuCounty(R.drawable.ic_penghu_county, "澎湖縣"),
    KinmenCounty(R.drawable.ic_kinmen_county, "金門縣"),
    LienchiangCounty(R.drawable.ic_lienchiang_county, "連江縣");

    companion object {
        fun getEnum(content: String): FilterCity = values().first() { content == it.content }
    }
}