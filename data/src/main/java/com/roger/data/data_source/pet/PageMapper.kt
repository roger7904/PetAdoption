package com.roger.data.data_source.pet

enum class PageMapper(val page: Int, val top: Int, val skip: Int, val total: Int) {
    ONE(1, 200, 0, 10),
    TWO(2, 200, 200, 10),
    THREE(3, 200, 400, 10),
    FOUR(4, 200, 600, 10),
    FIVE(5, 200, 800, 10),
    SIX(6, 200, 1000, 10),
    SEVEN(7, 200, 1200, 10),
    EIGHT(8, 200, 1400, 10),
    NINE(9, 200, 1600, 10),
    TEN(10, 200, 1800, 10);

    companion object {
        fun getPage(page: Int): PageMapper = values().first() { page == it.page }
    }
}