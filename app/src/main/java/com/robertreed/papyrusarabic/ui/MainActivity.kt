package com.robertreed.papyrusarabic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.animations.AnimResources

class MainActivity : AppCompatActivity() {

    private val viewModel :MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment == null) {
            val pageLiveData = viewModel.currentPage()
            pageLiveData.observe(this, Observer {
                if(viewModel.hasPageLoaded()) {
                    pageLiveData.removeObservers(this)
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container, viewModel.fragmentSelector())
                        .commit()
                }
            })
        }
    }

    fun replacePage(owner: LifecycleOwner, anim: AnimResources) {
        Log.i("MAIN", "replacePageCalled")
        if(viewModel.hasPageLoaded()) {
            Log.i("MAIN", "pageReplaced")
            viewModel.currentPage().removeObservers(owner)
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(anim.enter, anim.exit)
                .replace(FRAGMENT_CONTAINER, viewModel.fragmentSelector())
                .commit()
        }
    }
}
