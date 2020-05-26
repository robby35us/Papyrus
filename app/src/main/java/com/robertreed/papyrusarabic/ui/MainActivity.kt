package com.robertreed.papyrusarabic.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.destinations.*

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
            pageLiveData.observe(this, Observer {page ->
                if(page.pageType != null) {
                    val fragment = fragmentSelector(viewModel.getCurrentPageTypeName())
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit()
                }
            })
        }
    }

    private fun fragmentSelector(name:String): Fragment {
        return when (name) {
            "splash" -> SplashScreen.newInstance()
            "moduleSelection" -> ModuleSelectionFragment.newInstance()
            "moduleContent" -> ModuleContentFragment.newInstance()
            "moduleList" -> ModuleListFragment.newInstance()
            "lessonSelection" -> LessonSelectionFragment.newInstance()
            "lessonContent" -> LessonContentFragment.newInstance()
            "lessonImage" -> LessonImageFragment.newInstance()
            else -> SplashScreen.newInstance()
        }
    }
}
