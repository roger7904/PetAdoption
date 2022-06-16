package com.roger.domain.entity.pet

data class PetEntity(
    val id: Int? = null, // 動物的流水編號
    val subId: String? = null, // 動物的區域編號
    val areaId: Int? = null, // 動物所屬縣市代碼
    val shelterId: Int? = null, // 動物所屬收容所代碼
    val petPlace: String? = null, // 動物的實際所在地
    val kind: String? = null, // 動物的類型
    val sex: String? = null, // 動物性別
    val bodyType: String? = null, // 動物體型
    val colour: String? = null, // 動物毛色
    val age: String? = null, // 動物年紀
    val sterilization: String? = null, // 是否絕育
    val vaccine: String? = null, // 是否施打狂犬病疫苗
    val foundPlace: String? = null, // 動物尋獲地
    val title: String? = null, // 動物網頁標題
    val status: String? = null, // 動物狀態
    val remark: String? = null, // 資料備註
    val caption: String? = null, // 其他說明
    val adoptOpenDate: String? = null, // 開放認養時間(起)
    val adoptClosedDate: String? = null, // 開放認養時間(迄)
    val infoUpdateTime: String? = null, // 動物資料異動時間
    val infoCreateTime: String? = null, // 動物資料建立時間
    val shelterName: String? = null, // 動物所屬收容所名稱
    val albumFile: String? = null, //  圖片名稱
    val albumUpdateTime: String? = null, // 圖片異動時間
    val cDate: String? = null, // 資料更新時間
    val shelterAddress: String? = null, // 地址
    val shelterTel: String? = null, // 連絡電話
    val variety: String? = null, // 動物品種
    val weatherMin: String? = null, // 最低溫度
    val weatherMax: String? = null, // 最高溫度
    val weatherWx: String? = null, // 天氣現象
)