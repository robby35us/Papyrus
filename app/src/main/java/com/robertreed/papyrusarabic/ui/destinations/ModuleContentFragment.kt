package com.robertreed.papyrusarabic.ui.destinations

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

class ModuleContentFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var moduleId: TextView
    private lateinit var header: TextView
    private lateinit var content1: TextView
    private lateinit var content2: TextView
    private lateinit var content3: TextView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_module_content, container, false)

        val page = viewModel.getCurrentPage()

        moduleId = view.findViewById(R.id.context)
        moduleId.setText(page.number)

        header = view.findViewById(R.id.header)
        header.text = page.header

        content1 = view.findViewById(R.id.content1)
        content1.text = page.content1

        content2 = view.findViewById(R.id.content2)
        content2.text = page.content2

        content3 = view.findViewById(R.id.content3)
        content3.text = page.content3

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navOutOfModule()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            findNavController().navigate(R.id.action_moduleIntroFragment_to_moduleContentOverviewFragment)
        }
        navRight.visibility = View.INVISIBLE
        navRight.isEnabled = false

        return view
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.locationPreviouslyReached()) {
            content1.alpha = 1.0f
            content2.alpha = 1.0f
            content3.alpha = 1.0f
            navRight.visibility = View.VISIBLE
            navRight.isEnabled = true
        }
        else {
            ObjectAnimator.ofFloat(content1, "alpha", 1.0f).apply {
                duration = 750
                startDelay = 500
                start()
            }
            ObjectAnimator.ofFloat(content2, "alpha", 1.0f).apply {
                duration = 750
                startDelay = 3500
                start()
            }
            ObjectAnimator.ofFloat(content3, "alpha", 1.0f).apply {
                duration = 750
                startDelay = 7000
                start()
            }
            Handler().postDelayed({
                navRight.visibility = View.VISIBLE
                navRight.isEnabled = true
            }, 7000)
        }
    }
}