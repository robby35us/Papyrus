package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Lesson::class,
    parentColumns = arrayOf("moduleLesson"),
    childColumns = arrayOf("id")
), ForeignKey(
    entity = Lesson::class,
    parentColumns = arrayOf("lesson1"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Lesson::class,
    parentColumns = arrayOf("lesson2"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Lesson::class,
    parentColumns = arrayOf("lesson3"),
    childColumns = arrayOf("id"))])
data class Module(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var title: String,
                  var moduleLesson: UUID,
                  var lesson1: UUID,
                  var lesson2: UUID,
                  var lesson3: UUID)