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
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.*
import com.robertreed.papyrusarabic.ui.animations.PageTextAnimUtil

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
        header = view.findViewById(R.id.header)
        content1 = view.findViewById(R.id.content1)
        content2 = view.findViewById(R.id.content2)
        content3 = view.findViewById(R.id.content3)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            val anim = if(viewModel.hasPrevPage()) {
                viewModel.navToPrevPage()
                ANIM_TO_PREV
            } else {
                viewModel.navOutOfLesson()
                ANIM_OUT_OF
            }
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, anim)
            })
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.visibility = View.INVISIBLE
        navRight.isEnabled = false
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_NEXT)
            })
        }

        pageLiveData.observe(viewLifecycleOwner, Observer {page ->
            if(viewModel.hasPageLoaded()) {
                pageLiveData.removeObservers(viewLifecycleOwner)
                context.text = page.number.toString()
                header.text = page.header
                content1.text = page.content1
                content2.text = page.content2
                content3.text = page.content3
            }
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        PageTextAnimUtil.fadeInText(
            listOf(content1, content2, content3),
            navRight,
            viewModel.locationPreviouslyReached()
        )
    }

    companion object {
        fun newInstance() = LessonContentFragment()
    }
}
