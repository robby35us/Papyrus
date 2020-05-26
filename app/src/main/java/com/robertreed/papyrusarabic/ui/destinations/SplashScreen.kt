package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.ANIM_FADE
import com.robertreed.papyrusarabic.ui.FRAGMENT_CONTAINER
import com.robertreed.papyrusarabic.ui.MainViewModel

class SplashScreen: Fragment(), View.OnClickListener {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var screen: ConstraintLayout
    private lateinit var titleView: TextView
    private lateinit var subtitleView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        screen = view.findViewById(R.id.splash_screen)
        screen.isEnabled = false
        screen.setOnClickListener(this)

        titleView = view.findViewById(R.id.header)
        titleView.isEnabled = false
        titleView.setOnClickListener(this)

        subtitleView = view.findViewById(R.id.sub_header)
        subtitleView.isEnabled = false
        subtitleView.setOnClickListener(this)

        val pageLiveData = viewModel.currentPage()
        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if (page.lessonId != null) {
                screen.isEnabled = true
                titleView.isEnabled = true
                subtitleView.isEnabled = true
            }
        })

        return view
    }

    override fun onClick(v: View?) {
        viewModel.navToNextPage()
        viewModel.currentPage().observe(viewLifecycleOwner, Observer {page ->
            if(page.pageType != null) {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(
                        ANIM_FADE.enter, ANIM_FADE.exit, ANIM_FADE.popEnter, ANIM_FADE.popExit
                    )
                    .replace(FRAGMENT_CONTAINER, viewModel.fragmentSelector())
                    .addToBackStack("splash")
                    .commit()
            }
        })
    }

    companion object {
        fun newInstance() = SplashScreen()
    }
}