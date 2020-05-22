package com.robertreed.android.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Image(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var resourceName: String,
                 var contentDescription: String)