package com.robertreed.papyrusarabic.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Lesson
import java.util.*

@Dao
interface LessonDao {

    @Query("SELECT * FROM lesson WHERE id=(:id)")
    fun getLesson(id: UUID): LiveData<Lesson?>

    @Query("SELECT * FROM lesson WHERE moduleId=(:moduleId) ORDER BY number")
    fun getLessonsByModuleId(moduleId: UUID): LiveData<List<Lesson>>

    @Insert
    fun insert(lesson: Lesson)
}