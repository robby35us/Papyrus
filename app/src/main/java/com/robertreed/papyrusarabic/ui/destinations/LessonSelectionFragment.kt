package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.LESSON_PAGE_NUM_OFFSET
import com.robertreed.papyrusarabic.ui.MainViewModel
import com.robertreed.papyrusarabic.ui.NUM_LESSONS_PER_MODULE

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
            requireActivity().supportFragmentManager.popBackStack()
        }

        gotoButton = view.findViewById(R.id.goto_button)
        gotoButton.isEnabled = false
        gotoButton.setOnClickListener {
            viewModel.navIntoLesson()
            requireActivity().supportFragmentManager.popBackStack()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.isEnabled = false
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            requireActivity().supportFragmentManager.popBackStack()
        }

        val pageLiveData = viewModel.currentPage()
        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            context.text = page?.number.toString()
            content.text = page?.header
            subHeader.text = page?.sub_header

            navLeft.isEnabled = true
            navRight.isEnabled = viewModel.hasNextPage()

            if(viewModel.isLessonCompleted()) {
                starImage.setImageResource(android.R.drawable.btn_star_big_on)
                gotoButton.setText(R.string.restart_lesson)
            } else {
                gotoButton.setText(R.string.start_lesson)
                starImage.setImageResource(android.R.drawable.btn_star_big_off)
            }
        })
        return view
    }

    companion object {
        fun newInstance() = LessonSelectionFragment()
    }
}

