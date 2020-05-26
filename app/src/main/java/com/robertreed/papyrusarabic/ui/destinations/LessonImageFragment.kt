package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

class LessonImageFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var context : TextView
    private lateinit var image: ImageView
    private lateinit var header: TextView
    private lateinit var subHeader: TextView

    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lesson_image, container, false)

        context = view.findViewById(R.id.context)
        image = view.findViewById(R.id.image)
        header = view.findViewById(R.id.header)
        subHeader = view.findViewById(R.id.sub_header)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
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
            context.text = page.number.toString()
            header.text = page.header
            subHeader.text = page.sub_header
            navLeft.isEnabled = true
            navRight.isEnabled = true
        })

        return view
    }

    companion object {
        fun newInstance() = LessonImageFragment()
    }
}
