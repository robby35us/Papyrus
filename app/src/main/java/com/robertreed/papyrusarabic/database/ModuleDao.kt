package com.robertreed.papyrusarabic.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.robertreed.papyrusarabic.model.Module
import java.util.*

@Dao
interface ModuleDao {

    @Query("SELECT * FROM module WHERE id=(:id)")
    fun getModule(id: UUID): Module?

    @Query("SELECT * FROM module WHERE number=(:number)")
    fun getModuleByNumber(number: Int) : Module?

    @Query("SELECT * FROM module ORDER BY number")
    fun getModules(): List<Module>

    @Insert
    fun insert(module: List<Module>)
}