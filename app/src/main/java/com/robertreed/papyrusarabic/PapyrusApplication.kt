package com.robertreed.papyrusarabic

import android.app.Application
import com.robertreed.papyrusarabic.repository.PapyrusRepository

class PapyrusApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        PapyrusRepository.initialize(this)
    }
}