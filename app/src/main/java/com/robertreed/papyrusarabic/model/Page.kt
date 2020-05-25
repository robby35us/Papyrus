package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = Lesson::class,
                                parentColumns = arrayOf("id"),
                                childColumns = arrayOf("lessonId")),
                        ForeignKey(entity = Image::class,
                                parentColumns = arrayOf("id"),
                                childColumns = arrayOf("image")),
                        ForeignKey(entity = PageType::class,
                                parentColumns = arrayOf("id"),
                                childColumns = arrayOf("pageType"))],
        indices = [Index(value = ["lessonId"], name = "page_lesson_id_index"),
                   Index(value = ["image"], name = "page_image_index"),
                   Index(value = ["pageType"], name = "page_page_type_index")])
data class Page(@PrimaryKey val id: UUID = UUID.randomUUID(),
                var lessonId: UUID? = null,
                var number: Int = 0,
                var image: UUID? = null,
                var header: String? = "",
                var sub_header: String? = "",
                var content1: String? = "",
                var content2: String? = "",
                var content3: String? = "",
                var pageType: UUID? = null
)