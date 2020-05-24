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
import androidx.navigation.fragment.findNavController
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel

class ModuleSelectionFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var cardView: CardView
    private lateinit var moduleId: TextView
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

        val page = viewModel.getCurrentPage()

        // load module card content

        cardView = view.findViewById(R.id.module_card)
        if(!viewModel.moduleSelectionAvailable()) {
            cardView.setCardBackgroundColor(ContextCompat.getColor(requireContext(),
                R.color.colorDisabled
            ))
        }

        moduleId  = view.findViewById(R.id.context)
        moduleId.text = page.number.toString()

        header = view.findViewById(R.id.header)
        header.text = page.header

        subHeader = view.findViewById(R.id.sub_header)
        subHeader.text = page.sub_header

        // load navigation content
        gotoButton = view.findViewById((R.id.goto_button))
        if (viewModel.moduleSelectionAvailable()){
            gotoButton.setText(R.string.start_module)
            gotoButton.setOnClickListener {
                viewModel.navIntoModule()
                findNavController().navigate(R.id.action_moduleSelectionFragment_to_moduleContentFragment)
            }
        }

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        if(viewModel.hasNextPage()) {
            navRight.isEnabled = true
            navRight.visibility = View.VISIBLE
            navRight.setOnClickListener {
                viewModel.navToNextPage()
                findNavController().navigate(R.id.action_moduleSelectionFragment_self)
            }
        }
        else {
            navRight.isEnabled = false
            navRight.visibility = View.INVISIBLE
        }
        return view
    }
}
