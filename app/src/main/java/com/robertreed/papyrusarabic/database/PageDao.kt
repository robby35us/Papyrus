package com.robertreed.papyrusarabic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Page
import java.util.*

@Dao
interface PageDao {

    @Query("SELECT * FROM page WHERE id=(:id)")
    fun getPage(id: UUID): LiveData<Page?>

    @Query("SELECT * FROM page WHERE lessonId=(:lessonId) ORDER BY number")
    fun getPagesByLessonID(lessonId: UUID): LiveData<List<Page>>

    @Insert
    fun insert(page: Page)
}