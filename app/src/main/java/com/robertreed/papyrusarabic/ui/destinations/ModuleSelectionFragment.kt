package com.robertreed.papyrusarabic.ui.destinations


import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

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

        cardView = view.findViewById(R.id.module_card)

        context  = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)
        subHeader = view.findViewById(R.id.sub_header)

        gotoButton = view.findViewById((R.id.goto_button))
        gotoButton.isEnabled = false
        gotoButton.setOnClickListener {
            viewModel.navIntoModule()
            requireActivity().supportFragmentManager.popBackStack()
        }

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
            context.text = page?.number.toString()
            header.text = page?.header
            subHeader.text = page?.sub_header

            if(viewModel.moduleSelectionAvailable()) {
                gotoButton.isEnabled = true
                gotoButton.setText(R.string.start_module)
            } else {
                cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                    R.color.colorDisabled
                ))
            }

            if(viewModel.hasNextPage())
                navRight.isEnabled = true
        })

        return view
    }

    companion object {
        fun newInstance() = ModuleSelectionFragment()
    }
}
