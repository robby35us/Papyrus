package com.robertreed.papyrusarabic.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Module(@PrimaryKey val id: UUID = UUID.randomUUID(),
                  val number: Int = 0,
                  val title: String = "",
                  val shortTitle: String = "",
                  val description: String = "")