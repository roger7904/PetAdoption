package com.roger.data.data_source.shelter

enum class ShelterPageMapper(val page: Int, val top: Int, val skip: Int, val total: Int) {
    ONE(1, 10, 0, 4),
    TWO(2, 10, 10, 4),
    THREE(3, 10, 20, 4),
    FOUR(4, 10, 30, 4);

    companion object {
        fun getPage(page: Int): ShelterPageMapper = values().first() { page == it.page }
    }
}