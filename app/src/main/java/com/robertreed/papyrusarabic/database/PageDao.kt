package com.robertreed.papyrusarabic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Page
import java.util.*

@Dao
interface PageDao {

    @Query("SELECT * FROM page WHERE id=(:id)")
    fun getPage(id: UUID): Page?

    @Query("SELECT id FROM page WHERE lessonId=(:lessonId) ORDER BY number")
    fun getPagesByLessonID(lessonId: UUID): List<Page>

    @Insert
    fun insert(page: Page)
}