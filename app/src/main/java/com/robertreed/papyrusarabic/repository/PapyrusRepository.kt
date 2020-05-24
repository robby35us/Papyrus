package com.robertreed.papyrusarabic.repository

import android.content.Context
import android.util.SparseArray
import androidx.room.Room
import com.robertreed.papyrusarabic.database.PapyrusDatabase
import com.robertreed.papyrusarabic.model.*
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.ModuleIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

private const val DATABASE_NAME = "papyrus-database"
private const val MODULE_NUM_OFFSET = 20
class PapyrusRepository private constructor(context: Context){

    private lateinit var loadJob: Job
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

    private var moduleIt = ModuleIterator(listOf())
    private var lessonIteratorArray = arrayOfNulls<LessonIterator>(0)
    private val pageIteratorArray = SparseArray<PageIterator>()

    fun clearDatabase() {
        database.clearAllTables()
    }

    suspend fun getModuleIterator(): ModuleIterator {
        loadJob.join()
        return moduleIt
    }

    suspend fun getLessonIterator(moduleIndex: Int) : LessonIterator {
        loadJob.join()
        if (lessonIteratorArray[moduleIndex] == null) {
            val moduleId = moduleIt.get(moduleIndex).id
            val lessonList = lessonDao.getLessonsByModuleId(moduleId)
            lessonIteratorArray[moduleIndex] = LessonIterator(lessonList)
        }
        return lessonIteratorArray[moduleIndex]!!
    }

    suspend fun getPageIterator(moduleIndex: Int, lessonIndex: Int): PageIterator {
        val pageOffset = moduleIndex * MODULE_NUM_OFFSET + lessonIndex
        if (pageIteratorArray[pageOffset] == null) {
            loadJob.join()
            val lessonIterator = getLessonIterator(moduleIndex)
            val lessonId = lessonIterator.get(lessonIndex).id
            val pageList = pageDao.getPagesByLessonID(lessonId)
            pageIteratorArray.setValueAt(pageOffset, PageIterator(pageList))
        }
        return pageIteratorArray[pageOffset]!!
    }

    fun insertModule(module: Module) {
        moduleDao.insert(listOf(module))
    }

    fun getModule(uuid: UUID): Module? {
        return moduleDao.getModule(uuid)
    }

    fun insertLesson(lesson: Lesson) {
        lessonDao.insert(lesson)
    }

    fun getLesson(uuid: UUID): Lesson? {
        return lessonDao.getLesson(uuid)
    }

    fun insertPageType(pageType: PageType) {
        pageTypeDao.insert(pageType)
    }

    fun getPageType(uuid: UUID): PageType? {
        return pageTypeDao.getPageType(uuid)
    }

    fun insertImage(image: Image) {
        imageDao.insert(image)
    }

    fun getImage(uuid: UUID): Image? {
        return imageDao.getImage(uuid)
    }

    fun insertPage(page: Page) {
        pageDao.insert(page)
    }

    fun getPage(uuid: UUID) : Page? {
        return pageDao.getPage(uuid)
    }

    companion object {
        private  var INSTANCE: PapyrusRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null)
                INSTANCE = PapyrusRepository(context)
        }

        fun get(): PapyrusRepository {
            if(INSTANCE?.moduleIt?.size() ?: 0 == 0) {
                INSTANCE?.loadJob = GlobalScope.launch {
                    INSTANCE?.moduleIt = ModuleIterator(INSTANCE?.moduleDao!!.getModules())
                    INSTANCE?.lessonIteratorArray =
                        arrayOfNulls<LessonIterator?>(INSTANCE!!.moduleIt.size())
                }
            }
            return INSTANCE
                ?: throw IllegalStateException("Repository Must be initialized")
        }
    }
}