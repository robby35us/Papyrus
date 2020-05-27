package com.robertreed.papyrusarabic.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.robertreed.papyrusarabic.model.*

@Database(entities = [Module::class, Lesson::class, Page::class, Image::class, PageType::class, Media::class],
          version = 5)
@TypeConverters(PapyrusTypeConverter::class)
abstract class PapyrusDatabase: RoomDatabase() {
    abstract fun imageDao(): ImageDao
    abstract fun lessonDao(): LessonDao
    abstract fun moduleDao(): ModuleDao
    abstract fun pageDao(): PageDao
    abstract fun pageTypeDao(): PageTypeDao
    abstract fun mediaDao(): MediaDao
}