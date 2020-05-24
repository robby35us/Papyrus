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

    private lateinit var contentLabel: TextView
    private lateinit var contentHeader: TextView
    private lateinit var contentDescription: TextView
    private lateinit var objectiveList: ListView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton
    private lateinit var adapter : ObjectiveListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_module_list, container, false)

        val page = viewModel.getCurrentPage()

        contentLabel = view.findViewById(R.id.context)
        contentLabel.text = page.number.toString()

        contentHeader = view.findViewById(R.id.header)
        contentHeader.text = page.header

        contentDescription = view.findViewById(R.id.sub_header)
        contentDescription.text = page.sub_header


        adapter = ObjectiveListAdapter(arrayOf(page.content1!!, page.content2!!, page.content3!!))

        objectiveList = view.findViewById(R.id.content_list)
        objectiveList.adapter = adapter
        objectiveList.isEnabled = false

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            findNavController().navigateUp()
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            findNavController().navigate(R.id.action_moduleContentOverviewFragment_to_lessonSelectionFragment)
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

    private inner class ObjectiveListAdapter(private val objectives: Array<String>) : BaseAdapter() {
        private var textViews = arrayListOf<View>()

        override fun getItem(position: Int): Any {
            return textViews[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return objectives.size
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            if(convertView == null)
                textViews.add(layoutInflater.inflate(R.layout.item_list, parent, false))
            else
                textViews.add(convertView)
            val textView = textViews[position].findViewById<TextView>(R.id.content)
            textView?.text = objectives[position]
            return textViews[position]
        }
    }
}
