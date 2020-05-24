package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

class LessonSelectionFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var context: TextView
    private lateinit var starImage: ImageView
    private lateinit var header: TextView
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

        val page = viewModel.getCurrentPage()

        context = view.findViewById(R.id.context)
        context.text = page.number.toString()

        if(viewModel.isLessonCompleted()) {
            starImage = view.findViewById(R.id.star_image)
            starImage.setImageResource(android.R.drawable.btn_star_big_on)
        }

        header = view.findViewById(R.id.header)
        header.text = page.header

        subHeader = view.findViewById(R.id.sub_header)
        subHeader.text = page.sub_header

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        if(viewModel.hasNextPage()) {
            navRight.setOnClickListener {
                viewModel.navToNextPage()
                findNavController().navigate(R.id.action_lessonSelectionFragment_self)
            }
        } else {
            Toast.makeText(requireContext(),
                R.string.sample_toast_onclick, Toast.LENGTH_SHORT).show()
        }

        gotoButton = view.findViewById(R.id.goto_button)
        gotoButton.setText(
            if(viewModel.isLessonCompleted())
                R.string.start_lesson
            else
                R.string.restart_lesson
        )

        gotoButton.setOnClickListener {
            viewModel.navIntoLesson()
            findNavController().navigate(R.id.action_lessonSelectionFragment_to_lessonContentFragment)
        }

        return view
    }

}

