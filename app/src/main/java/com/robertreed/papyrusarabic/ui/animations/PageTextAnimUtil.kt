package com.robertreed.papyrusarabic.ui.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.arch.core.util.Function
import com.robertreed.papyrusarabic.R

private const val INITIAL_DELAY = 500L
private const val DURATION = 750L
private const val PAUSE_DURATION = 1000L

class PageTextAnimUtil private constructor(){

    companion object {

        fun fadeInText(textViewList: List<TextView>, navRight: ImageView, locationReached: Boolean): Function<Unit, Unit> {
            if (locationReached) {
                for (view in textViewList)
                    view.alpha = 1.0f
                navRight.visibility = View.VISIBLE
                navRight.isEnabled = true
                return Function<Unit, Unit> { }
            } else {
                val animators = arrayOfNulls<Animator>(textViewList.size)
                for (i in textViewList.indices)
                     animators[i] = ObjectAnimator.ofFloat(textViewList[i], "alpha", 1.0f).apply {
                        duration = DURATION
                        startDelay = i * (DURATION + PAUSE_DURATION) + INITIAL_DELAY
                        start()
                    }
                val handler = Handler().apply {
                    this.postDelayed({
                        navRight.visibility = View.VISIBLE
                        navRight.isEnabled = true
                    }, (DURATION + PAUSE_DURATION) * textViewList.size + INITIAL_DELAY)
                }
                return Function<Unit, Unit>{
                    for(i in animators.indices) {
                        animators[i]!!.end()
                    }
                    handler.removeCallbacksAndMessages(null)
                    navRight.visibility = View.VISIBLE
                    navRight.isEnabled = true
                }
            }

        }

        fun animateListIn (context: Context, adapter: ContentListAdapter,
                           navRight: ImageView, prevReached: Boolean) {
            if(prevReached) {
                adapter.notifyDataSetChanged()
                navRight.visibility = View.VISIBLE
                navRight.isEnabled = true
            }
            else {
                var index = 0

                val animation = AnimationUtils.makeInChildBottomAnimation(context)
                animation.duration = DURATION
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
                            }, PAUSE_DURATION)
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
                }, INITIAL_DELAY)
            }
        }



        class ContentListAdapter(
            private val textList: Array<String?>,
            private val layoutInflater: LayoutInflater) : BaseAdapter() {
            private var textViews = arrayListOf<View>()

            override fun getItem(position: Int): Any {

                val textView = textViews[position].findViewById<TextView>(R.id.content)
                textView.text = textList[position]
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
                return textViews[position]
            }
        }
    }
}