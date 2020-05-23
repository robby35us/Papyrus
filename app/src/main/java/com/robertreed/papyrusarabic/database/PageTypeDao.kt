package com.robertreed.papyrusarabic.database

import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.PageType
import java.util.*

interface PageTypeDao {

    @Query("SELECT * FROM pagetype")
    fun getPageTypes(): List<PageType>

    @Insert
    fun insert(pageType: PageType)
}