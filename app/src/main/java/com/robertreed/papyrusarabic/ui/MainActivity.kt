package com.robertreed.papyrusarabic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.repository.LocationData

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val factory = MainViewModelFactory(LocationData(0,0,0),
                                           LocationData(0,0,0))

        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }
}
