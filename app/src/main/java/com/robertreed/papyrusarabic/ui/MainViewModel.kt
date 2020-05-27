package com.robertreed.papyrusarabic.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.model.Page
import com.robertreed.papyrusarabic.repository.LocationData
import com.robertreed.papyrusarabic.repository.PapyrusRepository
import com.robertreed.papyrusarabic.repository.iterators.LessonIterator
import com.robertreed.papyrusarabic.repository.iterators.PageIterator
import com.robertreed.papyrusarabic.ui.animations.AnimResources
import com.robertreed.papyrusarabic.ui.destinations.*

const val LESSON_PAGE_NUM_OFFSET = 1
const val MODULE_PAGE_NUM_OFFSET = 0
const val NUM_LESSONS_PER_MODULE = 3
const val FRAGMENT_CONTAINER = R.id.fragment_container

val ANIM_FADE =
    AnimResources(
        android.R.anim.fade_in,
        android.R.anim.fade_out
    )
val ANIM_INTO =
    AnimResources(
        R.anim.push_up_in,
        R.anim.push_up_out
    )
val ANIM_OUT_OF =
    AnimResources(
        R.anim.push_down_in,
        R.anim.push_down_out
    )
val ANIM_TO_NEXT =
    AnimResources(
        R.anim.slide_in_right,
        R.anim.slide_out_left
    )
val ANIM_TO_PREV =
    AnimResources(
        R.anim.slide_in_left,
        R.anim.slide_out_right
    )


class MainViewModel : ViewModel() {

    private val dummyPage = Page()
    private val repository = PapyrusRepository.get()
    private var moduleIt = repository.getModuleIterator()
    private var lessonIt = LessonIterator(MutableLiveData(listOf()))
    private var pageIt = PageIterator(MutableLiveData(listOf()))
    private var pageTypes = repository.getPageTypes()

    private var currentLocation = LocationData()
    private var farthestLocation = LocationData()

    private val pageData = MutableLiveData(dummyPage)
    private var pageDataLoaded = false

    init {
        loadPageData()
        pageData.observeForever {
            pageDataLoaded = moduleIt.isLoaded() && lessonIt.isLoaded()
                    && pageIt.isLoaded() && pageData.value!!.pageType != null
        }
    }

    fun atFarthestLocationReached() = compare(currentLocation, farthestLocation) == 0

    fun currentPage() = pageData

    fun fragmentSelector(): Fragment {
        return when (getCurrentPageTypeName()) {
            "splash" -> SplashScreen.newInstance()
            "moduleSelection" -> ModuleSelectionFragment.newInstance()
            "moduleContent" -> ModuleContentFragment.newInstance()
            "moduleList" -> ModuleListFragment.newInstance()
            "lessonSelection" -> LessonSelectionFragment.newInstance()
            "lessonContent" -> LessonContentFragment.newInstance()
            "lessonImage" -> LessonImageFragment.newInstance()
            "mediaPlayer" -> LessonMediaFragment.newInstance()
            else -> SplashScreen.newInstance()
        }
    }

    fun getCurrentPageTypeName() = pageTypes.get(pageData.value!!.pageType!!).name

    fun getMedia() = repository.getMediaIterator(
        currentLocation.moduleNum, currentLocation.lessonNum, currentLocation.pageNum
    )

    fun hasNextLesson() = lessonIt.hasNext()

    fun hasNextModule() = lessonIt.hasNext()

    fun hasNextPage() = pageIt.hasNext()

    fun hasPageLoaded() = pageDataLoaded && pageData.value!!.pageType != null

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
        if(pageIt.hasNext()) {
            currentLocation.pageNum += 1
            commitPageChange()
        }
    }

    fun navToPrevPage() {
        if(pageIt.hasPrevious()) {
            currentLocation.pageNum -= 1
            commitPageChange()
        }
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
        commitPageChange()
    }

    private fun commitPageChange() {
        if (compare(currentLocation, farthestLocation) > 0)
            farthestLocation = currentLocation
        pageData.postValue(dummyPage)
        pageDataLoaded = false
        loadPageData()
    }

    private fun compare(locationData1: LocationData, locationData2: LocationData): Int {

        if(locationData1.moduleNum == 0 && locationData2.moduleNum > 0)
            return when {
                locationData1.pageNum - MODULE_PAGE_NUM_OFFSET < locationData2.moduleNum -> -1
                locationData1.pageNum - MODULE_PAGE_NUM_OFFSET > locationData2.moduleNum -> 1
                else -> 0
            }
        else if(locationData1.moduleNum > 0 && locationData1.lessonNum == 0)
            return when {
                locationData1.pageNum - LESSON_PAGE_NUM_OFFSET < locationData2.lessonNum -> -1
                locationData1.pageNum - LESSON_PAGE_NUM_OFFSET > locationData2.lessonNum -> 1
                else -> 0
            }
        else return when {
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
        if (moduleIt.isLoaded()) {
            moduleIt.setIndex(currentLocation.moduleNum)
            lessonIt = repository.getLessonIterator(currentLocation.moduleNum)
        }else {
            moduleIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    moduleIt.setIndex(currentLocation.moduleNum)
                    lessonIt = repository.getLessonIterator(currentLocation.moduleNum)
                    loadPageData()
                }
            }
            return
        }


        if (lessonIt.isLoaded()) {
            lessonIt.setIndex(currentLocation.lessonNum)
            pageIt =
                repository.getPageIterator(currentLocation.moduleNum, currentLocation.lessonNum)
        } else {
            lessonIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    lessonIt.setIndex(currentLocation.lessonNum)
                    pageIt = repository.getPageIterator(
                        currentLocation.moduleNum,
                        currentLocation.lessonNum
                    )
                    loadPageData()
                }
            }
            return
        }

        if (pageIt.isLoaded()) {
            pageIt.setIndex(currentLocation.pageNum)
            pageData.postValue(pageIt.peek())
        } else
            pageIt.getLiveData().observeForever {
                if (it.isNotEmpty()) {
                    pageIt.setIndex(currentLocation.pageNum)
                    pageData.postValue(pageIt.peek())
                }
            }
        // return
    }
}