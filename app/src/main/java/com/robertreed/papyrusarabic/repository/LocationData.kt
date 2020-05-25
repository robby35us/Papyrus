package com.robertreed.papyrusarabic.repository

data class LocationData(
    var moduleNum: Int = 0,
    var moduleNumSet: Boolean = false,
    var lessonNum: Int = 0,
    var lessonNumSet: Boolean = false,
    var pageNum: Int = 0,
    var pageNumSet: Boolean = false
)