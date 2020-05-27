package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.*

class ModuleSelectionFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var cardView: CardView
    private lateinit var context: TextView
    private lateinit var header: TextView
    private lateinit var subHeader: TextView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton
    private lateinit var gotoButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_module_selection, container, false)
        Log.i("MODULE_SELECTION", "in onCreateView")

        val pageLiveData = viewModel.currentPage()

        cardView = view.findViewById(R.id.module_card)

        context  = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)
        subHeader = view.findViewById(R.id.sub_header)

        gotoButton = view.findViewById((R.id.goto_button))
        gotoButton.isEnabled = false
        gotoButton.setOnClickListener {
            viewModel.navIntoModule()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_INTO)
            })
        }

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            val anim = if(viewModel.currentPage().value!!.number > 1)
                ANIM_TO_PREV
            else
                ANIM_FADE
            viewModel.navToPrevPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, anim)
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


        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if (viewModel.hasPageLoaded()) {
                pageLiveData.removeObservers(viewLifecycleOwner)
                context.text = page.number.toString()
                header.text = page.header
                subHeader.text = page.sub_header
                navLeft.isEnabled = true

                if (viewModel.moduleSelectionAvailable()) {
                    gotoButton.isEnabled = true
                    gotoButton.setText(R.string.start_module)
                } else {
                    cardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.colorDisabled
                        )
                    )
                }

                if (viewModel.hasNextPage())
                    navRight.isEnabled = true
                navRight.visibility = View.VISIBLE
            }
        })
        return view
    }

    companion object {
        fun newInstance() = ModuleSelectionFragment()
    }
}
