package com.robertreed.papyrusarabic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robertreed.papyrusarabic.repository.LocationData

class MainViewModelFactory(private val farthestLocation: LocationData,
                           private val startLocation: LocationData)
    : ViewModelProvider.Factory{
    
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(farthestLocation, startLocation) as T
    }
}