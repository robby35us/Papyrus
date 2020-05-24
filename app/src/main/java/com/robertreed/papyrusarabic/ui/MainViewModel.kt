package com.robertreed.papyrusarabic.ui

import androidx.lifecycle.ViewModel
import com.robertreed.papyrusarabic.repository.LocationData
import com.robertreed.papyrusarabic.repository.LocationDataNavigator
import com.robertreed.papyrusarabic.repository.MODULE_PAGE_NUM_OFFSET

class MainViewModel : ViewModel() {

    private var startLocation = LocationData(0,0,0)
    private var farthestLocation = LocationData(0,0,0)
    private val navigator = LocationDataNavigator(startLocation)

    fun atFarthestLocationReached() = compare(navigator.getLocation(), farthestLocation) == 0

    fun getCurrentPage() = navigator.getPage()

    fun hasNextPage() = navigator.hasNextPage()

    fun hasPrevPage() = navigator.hasPrevPage()

    fun isLessonCompleted(): Boolean {
        val currentLocation = navigator.getLocation()
        navigator.nextLesson()
        val result = compare(farthestLocation, navigator.getLocation()) >= 0
        navigator.setLocation(currentLocation)
        return result
    }

    fun locationPreviouslyReached() = compare(farthestLocation, navigator.getLocation()) > 0

    fun markLessonComplete() {
        if(navigator.hasNextPage())
            throw IllegalStateException()
        else {
            val currentLocation = navigator.getLocation()
            navigator.nextLesson()
            farthestLocation = navigator.getLocation()
            navigator.setLocation(currentLocation)
        }
    }

    fun moduleSelectionAvailable()
            = navigator.getPage().number - MODULE_PAGE_NUM_OFFSET <= farthestLocation.moduleNum

    fun navIntoLesson() {
        navigator.navIntoLesson()
    }

    fun navIntoModule() {
        navigator.navIntoModule()
    }

    fun navToNextPage() {
        navigator.nextPage()
    }

    fun navToPrevPage() {
        navigator.prevPage()
    }

    fun navOutOfLesson() {
        navigator.navOutOfLesson()
    }

    fun navOutOfModule() {
        navigator.navOutOfModule()
    }

    fun numPagesInLesson() = navigator.numPages()

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
                    else-> 0
                }
            }
        }
    }
}