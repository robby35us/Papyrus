package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.model.Page
import com.robertreed.papyrusarabic.ui.LESSON_PAGE_NUM_OFFSET
import com.robertreed.papyrusarabic.ui.MainViewModel

class ModuleListFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var pageLiveData: LiveData<Page>
    private lateinit var context: TextView
    private lateinit var header: TextView
    private lateinit var subHeader: TextView
    private lateinit var contentList: ListView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton
    private lateinit var adapter : ContentListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_module_list, container, false)

        context = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)
        subHeader = view.findViewById(R.id.sub_header)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.isEnabled = false
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.isEnabled = false
        navRight.visibility = View.INVISIBLE
        navRight.setOnClickListener {
            val pageNum = viewModel.currentPage().value!!.number
            viewModel.navToNextPage()
            if(pageNum < LESSON_PAGE_NUM_OFFSET)
                findNavController().navigate(R.id.action_moduleListFragment_to_lessonSelectionFragment)
            else
                findNavController().navigate(R.id.action_moduleListFragment_to_moduleContentFragment)
        }

        adapter = ContentListAdapter()

        contentList = view.findViewById(R.id.content_list)
        contentList.adapter = adapter
        contentList.isEnabled = false

        pageLiveData = viewModel.currentPage()
        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            context.text = page?.number.toString()
            header.text = page?.header
            subHeader.text = page?.sub_header
            navLeft.isEnabled = true
        })

        return view
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.locationPreviouslyReached()) {
            adapter.notifyDataSetChanged()
            navRight.visibility = View.VISIBLE
            navRight.isEnabled = true
        }
        else {
            pageLiveData.observe(viewLifecycleOwner, Observer {
                var index = 0

                val animation = AnimationUtils.makeInChildBottomAnimation(requireContext())
                animation.duration = 1000
                animation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {
                        // left blank
                    }

                    override fun onAnimationEnd(animation: Animation?) {

                        if (index < adapter.count - 1) {
                            index += 1
                            Handler().postDelayed({
                                (adapter.getItem(index) as View).visibility = View.VISIBLE
                                (adapter.getItem(index) as View).startAnimation(animation)
                            }, 500)
                        } else {
                            navRight.isEnabled = true
                            navRight.visibility = View.VISIBLE
                        }
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        // left blank
                    }

                })

                Handler().postDelayed({
                    (adapter.getItem(index) as View).visibility = View.VISIBLE
                    (adapter.getItem(index) as View).startAnimation(animation)
                }, 500)
            })
        }
    }

    private inner class ContentListAdapter : BaseAdapter() {
        private var textViews = arrayListOf<View>()

        override fun getItem(position: Int): Any {
            return textViews[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(convertView == null)
                textViews.add(layoutInflater.inflate(R.layout.item_list, parent, false))
            else
                textViews.add(convertView)
            val textView = textViews[position].findViewById<TextView>(R.id.content)
            pageLiveData.observe(viewLifecycleOwner, Observer {
                    page -> textView.text = when (position) {
                        0 -> page?.content1
                        1 -> page?.content2
                        2 -> page?.content3
                        else -> throw ArrayIndexOutOfBoundsException()
                    }
            })
            return textViews[position]
        }
    }
}
