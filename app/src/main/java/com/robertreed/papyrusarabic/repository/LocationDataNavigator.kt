package com.robertreed.papyrusarabic.repository

import com.robertreed.papyrusarabic.model.Lesson
import com.robertreed.papyrusarabic.model.Module
import com.robertreed.papyrusarabic.model.Page
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.ModuleIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator

const val LESSON_PAGE_NUM_OFFSET = 2
const val MODULE_PAGE_NUM_OFFSET = 1

class LocationDataNavigator(startLocation: LocationData
            = LocationData(0, 0, 0)) {

    private val repository = PapyrusRepository.get()
    private var moduleIt: ModuleIterator
    private var lessonIt: LessonIterator
    private var pageIt: PageIterator

    init {
        moduleIt = repository.getModuleIterator()
        lessonIt = repository.getLessonIterator(startLocation.moduleNum)
        pageIt = repository.getPageIterator(
                startLocation.moduleNum, startLocation.lessonNum)
    }

    fun setItLocations(locationData: LocationData) {
        moduleIt.setIndex(locationData.moduleNum)
        lessonIt.setIndex(locationData.lessonNum)
        pageIt.setIndex(locationData.pageNum)
    }

    fun getModule() = moduleIt.peek()
    fun getLesson() = lessonIt.peek()
    fun getPage() = pageIt.peek()

    fun hasNextModule() = moduleIt.hasNext()
    fun hasPrevModule() = moduleIt.previousIndex() > 0

    fun hasNextLesson() = moduleIt.hasNext()
    fun hasPrevLesson() = moduleIt.previousIndex() > 0

    fun hasNextPage() = pageIt.hasNext()
    fun hasPrevPage() = pageIt.hasPrevious()

    fun nextPage(): Page {
       if(!pageIt.hasNext())
            navOutOfLesson()
        return pageIt.next()
    }

    fun prevPage(): Page {
        if(pageIt.hasPrevious())
            pageIt.previous()
        else {
            navOutOfLesson()
        }
        return pageIt.peek()
    }

    fun nextLesson(): Lesson {
        if(lessonIt.hasNext()) {
            navOutOfLesson()
        } else {
            navOutOfModule()
        }
        pageIt.next()
        return lessonIt.peek()
    }

    fun prevLesson(): Lesson {
        if(lessonIt.hasPrevious()) {
            navOutOfLesson()
        }else {
            navOutOfModule()
        }
        pageIt.previous()
        return lessonIt.peek()
    }

    fun nextModule(): Module {
        if (moduleIt.hasNext()) {
            navOutOfModule()
            pageIt.next()
        } else
            throw IllegalStateException()
        return moduleIt.peek()
    }

//    fun prevModule(): Module {
//        return Module()
//    }

    fun navOutOfModule() {
        val moduleNum = 0
        val lessonNum = 0
        val pageNum = moduleIt.peek().number + MODULE_PAGE_NUM_OFFSET

        moduleIt = repository.getModuleIterator()
        moduleIt.setIndex(moduleNum)

        lessonIt = repository.getLessonIterator(moduleNum)
        lessonIt.setIndex(lessonNum)

        pageIt = repository.getPageIterator(moduleNum, lessonNum)
        pageIt.setIndex(pageNum)
    }

    fun navIntoModule() {
        val moduleNum = pageIt.peek().number - MODULE_PAGE_NUM_OFFSET
        val lessonNum = 0
        val pageNum = 0
        moduleIt = repository.getModuleIterator()
        moduleIt.setIndex(moduleNum)

        lessonIt = repository.getLessonIterator(moduleNum)
        lessonIt.setIndex(lessonNum)

        pageIt = repository.getPageIterator(moduleNum, lessonNum)
        pageIt.setIndex(pageNum)
    }

    fun navOutOfLesson() {
        val moduleNum = moduleIt.peek().number
        val lessonNum = 0
        val pageNum = lessonIt.peek().number + LESSON_PAGE_NUM_OFFSET

        lessonIt = repository.getLessonIterator(moduleNum)
        lessonIt.setIndex(lessonNum)

        pageIt = repository.getPageIterator(moduleNum, lessonNum)
        pageIt.setIndex(pageNum)
    }

    fun navIntoLesson() {
        val moduleNum = moduleIt.peek().number
        val lessonNum = pageIt.peek().number - LESSON_PAGE_NUM_OFFSET
        val pageNum = 0

        lessonIt = repository.getLessonIterator(moduleNum)
        lessonIt.setIndex(lessonNum)

        pageIt = repository.getPageIterator(moduleNum, lessonNum)
        pageIt.setIndex(pageNum)
    }
}