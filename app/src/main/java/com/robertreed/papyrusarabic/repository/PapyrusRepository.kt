package com.robertreed.papyrusarabic.repository

import android.content.Context
import android.util.SparseArray
import androidx.core.util.set
import androidx.room.Room
import com.robertreed.papyrusarabic.database.PapyrusDatabase
import com.robertreed.papyrusarabic.model.*
import com.robertreed.papyrusarabic.repository.iterators.*
import java.util.*

private const val DATABASE_NAME = "papyrus-database"
private const val MODULE_NUM_OFFSET = 20
private const val MEDIA_OFFSET = 10
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
    private val mediaDao = database.mediaDao()

    private val moduleIt = ModuleIterator(moduleDao.getModules())
    private val lessonIteratorArray = SparseArray<LessonIterator>()
    private val pageIteratorArray = SparseArray<PageIterator>()
    private val mediaSparseArray = SparseArray<MediaIterator>()
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

        val pageIndex = moduleIndex * MODULE_NUM_OFFSET + lessonIndex

        if(pageIteratorArray[pageIndex, null] == null) {
            val lessonId = lessonIt.get(lessonIndex).id
            val pageIt = PageIterator(pageDao.getPagesByLessonID(lessonId))
            pageIteratorArray[pageIndex] = pageIt
        }

        return pageIteratorArray[pageIndex]
    }

    fun getMediaIterator(moduleIndex: Int, lessonIndex: Int, pageIndex: Int): MediaIterator {
        val pageIterator = getPageIterator(moduleIndex, lessonIndex)
        if(!pageIterator.isLoaded())
            throw IllegalAccessError()

        val mediaIndex = (moduleIndex * MODULE_NUM_OFFSET + lessonIndex) * MEDIA_OFFSET + pageIndex

        if(mediaSparseArray[mediaIndex, null] == null) {
            val pageId = pageIterator.get(pageIndex).id
            val mediaIterator = MediaIterator(mediaDao.getMediaByPageId(pageId))
            mediaSparseArray[mediaIndex] = mediaIterator
        }

        return mediaSparseArray[mediaIndex]
    }

    fun insertMedia(media: Media) {
        mediaDao.insert(media)
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