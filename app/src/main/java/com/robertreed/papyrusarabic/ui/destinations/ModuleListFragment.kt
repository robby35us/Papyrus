package com.robertreed.papyrusarabic.ui.destinations

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
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

        adapter = ContentListAdapter(textList)

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
        if(viewModel.locationPreviouslyReached()) {
            adapter.notifyDataSetChanged()
            navRight.visibility = View.VISIBLE
            navRight.isEnabled = true
        }
        else {
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
        }
    }

    override fun onStart() {
        super.onStart()
        pageLiveData.observe(viewLifecycleOwner, observer)
    }

    private inner class ContentListAdapter(var textList: Array<String?>) : BaseAdapter() {
        private var textViews = arrayListOf<View>()

        override fun getItem(position: Int): Any {
            return textViews[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return textList.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(convertView == null)
                textViews.add(layoutInflater.inflate(R.layout.item_list, parent, false))
            else
                textViews.add(convertView)
            val textView = textViews[position].findViewById<TextView>(R.id.content)
            pageLiveData.observe(viewLifecycleOwner, Observer {
                textView.text = textList[position]
            })
            return textViews[position]
        }
    }

    companion object {
        fun newInstance() = ModuleListFragment()
    }
}
