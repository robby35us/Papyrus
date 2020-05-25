package com.robertreed.papyrusarabic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.PageType
import java.util.*

@Dao
interface PageTypeDao {

    @Query("SELECT * FROM pagetype WHERE id=(:id)")
    fun getPageType(id: UUID): LiveData<PageType>

    @Query("SELECT * FROM pagetype")
    fun getPageTypes(): LiveData<List<PageType>>

    @Insert
    fun insert(pageType: PageType)
}