package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.model.Page
import com.robertreed.papyrusarabic.ui.ANIM_TO_NEXT
import com.robertreed.papyrusarabic.ui.ANIM_TO_PREV
import com.robertreed.papyrusarabic.ui.MainActivity
import com.robertreed.papyrusarabic.ui.MainViewModel
import com.robertreed.papyrusarabic.ui.animations.PageTextAnimUtil

class ModuleListFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var pageLiveData: LiveData<Page>
    private lateinit var context: TextView
    private lateinit var header: TextView
    private lateinit var subHeader: TextView
    private lateinit var contentList: ListView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton
    private lateinit var adapter : PageTextAnimUtil.Companion.ContentListAdapter
    private val textList: Array<String?> = arrayOfNulls<String?>(3)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_module_list, container, false)

        Log.i("MODULE_LIST_FRAGMENT", "in onCreateView")

        context = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)
        subHeader = view.findViewById(R.id.sub_header)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_PREV)
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

        adapter = PageTextAnimUtil.Companion.ContentListAdapter(textList, layoutInflater)

        contentList = view.findViewById(R.id.content_list)
        contentList.adapter = adapter
        contentList.isEnabled = false

        pageLiveData = viewModel.currentPage()
        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if(viewModel.hasPageLoaded()) {
                context.text = page.number.toString()
                header.text = page.header
                subHeader.text = page.sub_header
                textList[0] = page.content1
                textList[1] = page.content2
                textList[2] = page.content3
                navLeft.isEnabled = true
            }
        })

        return view
    }

    val observer = Observer<Page> {
        pageLiveData.removeObservers(viewLifecycleOwner)
        PageTextAnimUtil.animateListIn(
            requireContext(),
            adapter,
            navRight,
            viewModel.locationPreviouslyReached()
        )
    }

    override fun onStart() {
        super.onStart()
        pageLiveData.observe(viewLifecycleOwner, observer)
    }

    companion object {
        fun newInstance() = ModuleListFragment()
    }
}
