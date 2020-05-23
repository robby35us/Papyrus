package com.robertreed.papyrusarabic.repository

import android.content.Context
import android.util.SparseArray
import androidx.room.Room
import com.robertreed.papyrusarabic.database.PapyrusDatabase
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.ModuleIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator

private const val DATABASE_NAME = "papyrus-database"
private const val MODULE_NUM_OFFSET = 20
class PapyrusRepository private constructor(context: Context){

    private val database: PapyrusDatabase = Room.databaseBuilder(
        context.applicationContext,
        PapyrusDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val imageDao = database.imageDao()
    private val lessonDao = database.lessonDao()
    private val moduleDao = database.moduleDao()
    private val pageDao = database.pageDao()
    private val pageTypeDao = database.pageTypeDao()

    private val moduleIt =  ModuleIterator(moduleDao.getModules())
    private val lessonIteratorArray = arrayOfNulls<LessonIterator>(moduleIt.size())
    private val pageIteratorArray = SparseArray<PageIterator>()

    fun getModuleIterator(): ModuleIterator {
        return moduleIt
    }

    fun getLessonIterator(moduleIndex: Int) : LessonIterator {
        if (lessonIteratorArray[moduleIndex] == null) {
            val moduleId = moduleIt.get(moduleIndex).id
            val lessonList = lessonDao.getLessonsByModuleId(moduleId)
            lessonIteratorArray[moduleIndex] = LessonIterator(lessonList)
        }
        return lessonIteratorArray[moduleIndex]!!
    }

    fun getPageIterator(moduleIndex: Int, lessonIndex: Int): PageIterator {
        val pageOffset = moduleIndex * MODULE_NUM_OFFSET + lessonIndex
        if (pageIteratorArray[pageOffset] == null) {
            val lessonIterator = getLessonIterator(moduleIndex)
            val lessonId = lessonIterator.get(lessonIndex).id
            val pageList = pageDao.getPagesByLessonID(lessonId)
            pageIteratorArray.setValueAt(pageOffset, PageIterator(pageList))
        }
        return pageIteratorArray[pageOffset]!!
    }

    companion object {
        private  var INSTANCE: PapyrusRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null)
                INSTANCE = PapyrusRepository(context)
        }

        fun get(): PapyrusRepository {
            return INSTANCE
                ?: throw IllegalStateException("Repository Must be initialized")
        }
    }
}