package com.robertreed.papyrusarabic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robertreed.papyrusarabic.model.*

@Database(entities = [Module::class, LessonDao::class, Page::class, Image::class, PageType::class],
          version = 1)
@TypeConverters(PapyrusTypeConverter::class)
abstract  class PapyrusDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun lessonDao(): LessonDao
    abstract fun moduleDao(): ModuleDao
    abstract fun pageDao(): PageDao
    abstract fun pageTypeDao(): PageTypeDao
}