package com.robertreed.papyrusarabic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Image
import java.util.*

@Dao
interface ImageDao {

    @Query("SELECT * FROM image WHERE id=(:id)")
    fun getImage(id: UUID): Image?

    @Insert
    fun insert(image: Image)
}