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
import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.ui.MainViewModel


class ModuleListFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

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

        val page = viewModel.getCurrentPage()

        context = view.findViewById(R.id.context)
        context.text = page.number.toString()

        header = view.findViewById(R.id.header)
        header.text = page.header

        subHeader = view.findViewById(R.id.sub_header)
        subHeader.text = page.sub_header


        adapter = ContentListAdapter(arrayOf(page.content1!!, page.content2!!, page.content3!!))

        contentList = view.findViewById(R.id.content_list)
        contentList.adapter = adapter
        contentList.isEnabled = false

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            findNavController().navigate(R.id.action_moduleListFragment_to_lessonSelectionFragment)
        }
        navRight.isEnabled = false
        navRight.visibility = View.INVISIBLE

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
            var index = 0

            val animation = AnimationUtils.makeInChildBottomAnimation(requireContext())
            animation.duration = 1000
            animation.setAnimationListener(object: Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                    // left blank
                }

                override fun onAnimationEnd(animation: Animation?) {

                    if(index < adapter.count - 1) {
                        index += 1
                        Handler().postDelayed({
                            (adapter.getItem(index) as View).visibility = View.VISIBLE
                            (adapter.getItem(index) as View).startAnimation(animation)
                        }, 500)
                    }
                    else {
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

    private inner class ContentListAdapter(private val listItems: Array<String>) : BaseAdapter() {
        private var textViews = arrayListOf<View>()

        override fun getItem(position: Int): Any {
            return textViews[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listItems.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(convertView == null)
                textViews.add(layoutInflater.inflate(R.layout.item_list, parent, false))
            else
                textViews.add(convertView)
            val textView = textViews[position].findViewById<TextView>(R.id.content)
            textView?.text = listItems[position]
            return textViews[position]
        }
    }
}
