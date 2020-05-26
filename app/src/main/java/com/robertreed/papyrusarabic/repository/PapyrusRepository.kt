package com.robertreed.papyrusarabic.repository

import android.content.Context
import android.util.SparseArray
import androidx.core.util.set
import androidx.room.Room
import com.robertreed.papyrusarabic.database.PapyrusDatabase
import com.robertreed.papyrusarabic.model.*
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.ModuleIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator
import com.robertreed.papyrusarabic.repository.iterators.PageTypes
import java.util.*

private const val DATABASE_NAME = "papyrus-database"
private const val MODULE_NUM_OFFSET = 20
class PapyrusRepository private constructor(context: Context){

    private val database: PapyrusDatabase = Room.databaseBuilder(
        context.applicationContext,
        PapyrusDatabase::class.java,
        DATABASE_NAME
    ).fallbackToDestructiveMigration().build()

    private val imageDao = database.imageDao()
    private val lessonDao = database.lessonDao()
    private val moduleDao = database.moduleDao()
    private val pageDao = database.pageDao()
    private val pageTypeDao = database.pageTypeDao()

    private var moduleIt = ModuleIterator(moduleDao.getModules())
    private var lessonIteratorArray = SparseArray<LessonIterator>()
    private val pageIteratorArray = SparseArray<PageIterator>()
    private val pageTypes = PageTypes(pageTypeDao.getPageTypes())

    fun clearDatabase() {
        database.clearAllTables()
    }

    fun getPageTypes() = pageTypes

    fun getModuleIterator() = moduleIt

    fun getLessonIterator(moduleIndex: Int): LessonIterator {
        if(!moduleIt.isLoaded())
            throw IllegalAccessError()
        if(lessonIteratorArray[moduleIndex, null] == null) {
            val moduleId = moduleIt.get(moduleIndex).id
            val iterator = LessonIterator(lessonDao.getLessonsByModuleId(moduleId))
            lessonIteratorArray[moduleIndex] = iterator
        }
        return lessonIteratorArray[moduleIndex]
    }

    fun getPageIterator(moduleIndex: Int, lessonIndex: Int): PageIterator {
        val lessonIt = getLessonIterator(moduleIndex)
        if(!lessonIt.isLoaded())
            throw IllegalAccessError()

        val pageOffset = moduleIndex * MODULE_NUM_OFFSET + lessonIndex

        if(pageIteratorArray[pageOffset, null] == null) {
            val lessonId = lessonIt.get(lessonIndex).id
            val pageIt = PageIterator(pageDao.getPagesByLessonID(lessonId))
            pageIteratorArray[pageOffset] = pageIt
        }

        return pageIteratorArray[pageOffset]
    }

    fun insertModule(module: Module) {
        moduleDao.insert(listOf(module))
    }

    fun getModule(uuid: UUID): Module? {
        return moduleDao.getModule(uuid).value
    }

    fun insertLesson(lesson: Lesson) {
        lessonDao.insert(lesson)
    }

    fun getLesson(uuid: UUID): Lesson? {
        return lessonDao.getLesson(uuid).value
    }

    fun insertPageType(pageType: PageType) {
        pageTypeDao.insert(pageType)
    }

    fun getPageType(uuid: UUID): PageType? {
        return pageTypeDao.getPageType(uuid).value
    }

    fun insertImage(image: Image) {
        imageDao.insert(image)
    }

    fun getImage(uuid: UUID): Image? {
        return imageDao.getImage(uuid).value
    }

    fun insertPage(page: Page) {
        pageDao.insert(page)
    }

    fun getPage(uuid: UUID) : Page? {
        return pageDao.getPage(uuid).value
    }

    companion object {
        private  var INSTANCE: PapyrusRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null)
                INSTANCE = PapyrusRepository(context)
        }
        fun get(): PapyrusRepository {
            return INSTANCE ?: throw IllegalStateException("Repository Must be initialized")
        }
    }
}