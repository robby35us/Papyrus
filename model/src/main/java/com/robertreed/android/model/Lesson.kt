package com.robertreed.android.model

import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page1"),
    childColumns = arrayOf("id")
), ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page2"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page3"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page4"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page5"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page6"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page7"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page8"),
    childColumns = arrayOf("id")
),ForeignKey(
    entity = Page::class,
    parentColumns = arrayOf("page9"),
    childColumns = arrayOf("id")
)])
data class Lesson(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  var name: String,
                  var page1: UUID,
                  var page2: UUID,
                  var page3: UUID,
                  @Nullable var page4: UUID,
                  @Nullable var page5: UUID,
                  @Nullable var page6: UUID,
                  @Nullable var page7: UUID,
                  @Nullable var page8: UUID,
                  @Nullable var page9: UUID)
