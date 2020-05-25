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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

class LessonContentFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var context: TextView
    private lateinit var header: TextView
    private lateinit var content1: TextView
    private lateinit var content2: TextView
    private lateinit var content3: TextView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lesson_content, container, false)

        val pageLiveData = viewModel.currentPage()


        context = view.findViewById(R.id.context)
        pageLiveData.observe(viewLifecycleOwner, Observer {
                page -> context.text = page?.number.toString()
        })

        header = view.findViewById(R.id.header)
        pageLiveData.observe(viewLifecycleOwner, Observer {
                page -> header.text = page?.header
        })

        content1 = view.findViewById(R.id.content1)
        pageLiveData.observe(viewLifecycleOwner, Observer {
                page -> content1.text = page?.content1
        })

        content2 = view.findViewById(R.id.content2)
        pageLiveData.observe(viewLifecycleOwner, Observer {
                page -> content2.text = page?.content2
        })

        content3 = view.findViewById(R.id.content3)
        pageLiveData.observe(viewLifecycleOwner, Observer {
                page -> content3.text = page?.content3
        })

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            if(viewModel.hasPrevPage() == true)
                viewModel.navToPrevPage()
            else
                viewModel.navOutOfLesson()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.setOnClickListener {
            if (viewModel.hasNextPage() == true) {
                //viewModel.navToNextPage()
                //findNavController().navigate(R.id.l)
            } else {
                viewModel.markLessonComplete()
                val numPagesToPop = viewModel.numPagesInLesson()
                viewModel.navOutOfLesson()
                for (i in 0 until numPagesToPop!!)
                    findNavController().popBackStack()
            }
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
