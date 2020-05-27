package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.*

class LessonSelectionFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var context: TextView
    private lateinit var item: View
    private lateinit var starImage: ImageView
    private lateinit var content: TextView
    private lateinit var subHeader: TextView
    private lateinit var gotoButton: Button
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_lesson_selection, container, false)

        context = view.findViewById(R.id.context)

        item = view.findViewById(R.id.item)
        item.visibility = View.VISIBLE

        starImage = item.findViewById(R.id.star_image)
        content = item.findViewById(R.id.content)

        subHeader = view.findViewById(R.id.sub_header)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_PREV)
            })
        }

        gotoButton = view.findViewById(R.id.goto_button)
        gotoButton.isEnabled = false
        gotoButton.setOnClickListener {
            viewModel.navIntoLesson()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_INTO)
            })
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.isEnabled = false
        navRight.visibility = View.INVISIBLE
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_NEXT)
            })
        }

        val pageLiveData = viewModel.currentPage()
        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if(viewModel.hasPageLoaded()) {
                pageLiveData.removeObservers(viewLifecycleOwner)
                context.text = page.number.toString()
                content.text = page.header
                subHeader.text = page.sub_header

                navLeft.isEnabled = true

                if (viewModel.isLessonCompleted()) {
                    starImage.setImageResource(android.R.drawable.btn_star_big_on)
                    gotoButton.setText(R.string.restart_lesson)
                    navRight.isEnabled = true
                    navRight.visibility = View.VISIBLE
                } else {
                    gotoButton.setText(R.string.start_lesson)
                    starImage.setImageResource(android.R.drawable.btn_star_big_off)
                }
            }
        })
        return view
    }

    companion object {
        fun newInstance() = LessonSelectionFragment()
    }
}

