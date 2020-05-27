package com.robertreed.papyrusarabic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Media
import java.util.*

@Dao
interface MediaDao {

    @Query("SELECT * From media WHERE page=(:pageId)")
    fun getMediaByPageId(pageId: UUID): LiveData<List<Media>>

    @Insert
    fun insert(media: Media)
}