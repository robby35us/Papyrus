package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = Page::class,
                                  parentColumns = arrayOf("id"),
                                  childColumns = arrayOf("page"))],
        indices = [Index(value = arrayOf("page"), name = "page_index")])
data class Media(@PrimaryKey var id: UUID = UUID.randomUUID(),
                 var page: UUID? = null,
                 var name: String? = null,
                 var imageName: String? = null,
                 var soundName: String? = null)