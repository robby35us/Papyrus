package com.robertreed.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class PageType(@PrimaryKey val id: UUID = UUID.randomUUID(),
                    var name: String)