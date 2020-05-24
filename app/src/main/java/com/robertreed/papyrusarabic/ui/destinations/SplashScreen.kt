package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
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
        screen.setOnClickListener(this)

        titleView = view.findViewById(R.id.title_text)
        titleView.setOnClickListener(this)

        subtitleView = view.findViewById(R.id.subtitle_text)
        subtitleView.setOnClickListener(this)

        return view
    }

    override fun onClick(v: View?) {
        viewModel.navToNextPage()
        findNavController().navigate(R.id.action_splashScreen_to_moduleSelectionFragment)
    }
}