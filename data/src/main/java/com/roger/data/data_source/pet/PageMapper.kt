package com.roger.data.data_source.pet

enum class PageMapper(val page: Int, val top: Int, val skip: Int, val total: Int) {
    ONE(1, 100, 0, 10),
    TWO(2, 100, 100, 10),
    THREE(3, 100, 200, 10),
    FOUR(4, 100, 300, 10),
    FIVE(5, 100, 400, 10),
    SIX(6, 100, 500, 10),
    SEVEN(7, 100, 600, 10),
    EIGHT(8, 100, 700, 10),
    NINE(9, 100,800, 10),
    TEN(10, 100, 900, 10);

    companion object {
        fun getPage(page: Int): PageMapper = values().first() { page == it.page }
    }
}