package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = Lesson::class,
                                parentColumns = arrayOf("lessonId"),
                                childColumns = arrayOf("id")),
                        ForeignKey(entity = PageType::class,
                                parentColumns = arrayOf("pageType"),
                                childColumns = arrayOf("id"))])
data class Page(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var lessonId: UUID,
                var number: Int,
                var label: String,
                var image: UUID,
                var header: String,
                var contentOther: String = "",
                var content1 : String = "",
                var content2 : String = "",
                var content3 : String = "",
                var pageType: PageType
)