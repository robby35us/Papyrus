package com.robertreed.papyrusarabic.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robertreed.papyrusarabic.model.Page
import com.robertreed.papyrusarabic.repository.LocationData
import com.robertreed.papyrusarabic.repository.PapyrusRepository
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator

const val LESSON_PAGE_NUM_OFFSET = 2
const val MODULE_PAGE_NUM_OFFSET = 0
const val NUM_LESSONS_PER_MODULE = 3


class MainViewModel : ViewModel() {

    private val repository = PapyrusRepository.get()
    private var moduleIt = repository.getModuleIterator()
    private var lessonIt = LessonIterator(MutableLiveData(listOf()))
    private var pageIt = PageIterator(MutableLiveData(listOf()))
    private var pageTypes = repository.getPageTypes()

    private var currentLocation = LocationData()
    private var farthestLocation = LocationData()

    private val pageData = MutableLiveData(Page())
    private var pageDataLoaded = false

    init {
        loadPageData()
        pageData.observeForever {
            pageDataLoaded = pageData.value!!.lessonId != null
        }
    }

    fun atFarthestLocationReached() = compare(currentLocation, farthestLocation) == 0

    fun currentPage() = pageData

    fun getCurrentPageTypeName() = pageTypes.get(pageData.value!!.pageType!!).name

    fun hasNextLesson() = lessonIt.hasNext()

    fun hasNextModule() = lessonIt.hasNext()

    fun hasNextPage() = pageIt.hasNext()

    fun hasPrevLesson() = lessonIt.hasPrevious()

    fun hasPrevModule() = moduleIt.hasPrevious()

    fun hasPrevPage() = pageIt.hasPrevious()

    fun isLessonCompleted() = compare(currentLocation, farthestLocation) >= 0

    fun locationPreviouslyReached() = compare(currentLocation, farthestLocation) > 0

    fun markLessonComplete() {

    }

    fun moduleSelectionAvailable() =
        currentLocation.moduleNum == 0 && currentLocation.lessonNum == 0 &&
                (currentLocation.pageNum - MODULE_PAGE_NUM_OFFSET <= farthestLocation.moduleNum ||
                 farthestLocation.moduleNum == 0)

    fun navIntoLesson() {
        currentLocation.lessonNum = currentLocation.pageNum - LESSON_PAGE_NUM_OFFSET
        currentLocation.pageNum = 0
        commitPageChange()
    }

    fun navIntoModule() {
        currentLocation.moduleNum = currentLocation.pageNum - MODULE_PAGE_NUM_OFFSET
        currentLocation.lessonNum = 0
        currentLocation.pageNum = 0
        commitPageChange()
    }



    fun navOutOfLesson(commit: Boolean = true) {
        currentLocation.pageNum = currentLocation.lessonNum + LESSON_PAGE_NUM_OFFSET
        currentLocation.lessonNum = 0
        if (commit)
            commitPageChange()
    }

    fun navOutOfModule(commit: Boolean = true) {
        currentLocation.pageNum = currentLocation.moduleNum + MODULE_PAGE_NUM_OFFSET
        currentLocation.moduleNum = 0
        currentLocation.lessonNum = 0
        if (commit)
            commitPageChange()
    }

    fun navToNextPage() {
        if (!pageIt.hasNext())
            navOutOfLesson(false)
        if (!pageIt.hasNext())
            navOutOfModule(false)
        currentLocation.pageNum += 1
        commitPageChange()
    }

    fun navToPrevPage() {
        if (pageIt.hasPrevious())
            currentLocation.pageNum -= 1
        else
            navOutOfLesson(false)
        commitPageChange()
    }

    fun nextLesson() {
        if (lessonIt.hasNext()) {
            navOutOfLesson(false)
            currentLocation.pageNum += 1
        } else
            navOutOfModule(false)
        commitPageChange()
    }

    fun nextModule() {
        if (moduleIt.hasNext()) {
            navOutOfModule()
            currentLocation.pageNum + 1
        }
        commitPageChange()
    }

    fun numPagesInLesson() = pageIt.size()

    fun prevLesson() {
        if (lessonIt.hasPrevious()) {
            navOutOfLesson(false)
            currentLocation.pageNum -= 1
        } else
            navOutOfModule(false)
        commitPageChange()
    }

    fun prevModule() {
        if (moduleIt.hasPrevious()) {
            navOutOfModule()
            currentLocation.pageNum -= 1
        }
        commitPageChange()
    }

    fun setLocation(location: LocationData) {
        currentLocation = location
        pageDataLoaded = false
        loadPageData()
    }

    private fun commitPageChange() {
        if (compare(currentLocation, farthestLocation) > 0)
            farthestLocation = currentLocation
        pageData.postValue(Page())
        pageDataLoaded = false
        loadPageData()

    }

    private fun compare(locationData1: LocationData, locationData2: LocationData): Int {
        return when {
            locationData1.moduleNum < locationData2.moduleNum -> -1
            locationData1.moduleNum > locationData2.moduleNum -> 1
            else -> when {
                locationData1.lessonNum < locationData2.lessonNum -> -1
                locationData1.lessonNum > locationData2.lessonNum -> 1
                else -> when {
                    locationData1.pageNum < locationData2.pageNum -> -1
                    locationData1.pageNum > locationData2.pageNum -> 1
                    else -> 0
                }
            }
        }
    }

    private fun loadPageData() {
        if (moduleIt.isLoaded())
            lessonIt = repository.getLessonIterator(currentLocation.moduleNum)
        else
            moduleIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    lessonIt = repository.getLessonIterator(currentLocation.moduleNum)
                    loadPageData()
                }
            }

        if (lessonIt.isLoaded())
            pageIt =
                repository.getPageIterator(currentLocation.moduleNum, currentLocation.lessonNum)
        else
            lessonIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    pageIt = repository.getPageIterator(
                        currentLocation.moduleNum,
                        currentLocation.lessonNum
                    )
                    loadPageData()
                }
            }

        if (pageIt.isLoaded())
            pageData.postValue(pageIt.get(currentLocation.pageNum))
        else
            pageIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    pageData.postValue(pageIt.get(currentLocation.pageNum))
                    loadPageData()
                }

            }
    }
}