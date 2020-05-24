package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Module::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("moduleId"))],
    indices = [Index(value = ["moduleId"], name = "lesson_module_id_index")])
data class Lesson(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var moduleId: UUID,
                  var number: Int,
                  var name: String)
