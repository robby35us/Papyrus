package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Module::class,
    parentColumns = arrayOf("moduleId"),
    childColumns = arrayOf("id"))])
data class Lesson(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var moduleId: UUID,
                  var number: Int,
                  var name: String)
