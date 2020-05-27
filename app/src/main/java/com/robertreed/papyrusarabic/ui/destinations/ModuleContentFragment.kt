package com.robertreed.papyrusarabic.ui.destinations

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
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

class ModuleContentFragment : Fragment() {

    private val viewModel : MainViewModel by activityViewModels()

    private lateinit var context: TextView
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

        Log.i("MODULE_CONTENT_FRAGMENT", "in onCreateView")
        val pageLiveData = viewModel.currentPage()

        context = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)
        content1 = view.findViewById(R.id.content1)
        content2 = view.findViewById(R.id.content2)
        content3 = view.findViewById(R.id.content3)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            val anim = if (viewModel.hasPrevPage()) {
                viewModel.navToPrevPage()
                ANIM_TO_PREV
            } else {
                viewModel.navOutOfModule()
                ANIM_OUT_OF
            }
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, anim)
            })
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.isEnabled = false
        navRight.visibility = View.INVISIBLE
        navRight.setOnClickListener {
            if (viewModel.hasNextPage()) {
                viewModel.navToNextPage()
                viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                    (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_NEXT)
                })
            }
        }

        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if(viewModel.hasPageLoaded()) {
                pageLiveData.removeObservers(viewLifecycleOwner)
                context.text = page.number.toString()
                header.text = page.header
                content1.text = page.content1
                content2.text = page.content2
                content3.text = page.content3
                navLeft.isEnabled = true
            }
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.locationPreviouslyReached()) {
            content1.alpha = 1.0f
            content2.alpha = 1.0f
            content3.alpha = 1.0f
            if(viewModel.hasNextPage()) {
                navRight.visibility = View.VISIBLE
                navRight.isEnabled = true
            }
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

    companion object {
        fun newInstance() = ModuleContentFragment()
    }
}