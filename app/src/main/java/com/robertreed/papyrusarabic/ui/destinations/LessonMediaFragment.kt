package com.robertreed.papyrusarabic.ui.destinations

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

import com.robertreed.papyrusarabic.R
import com.robertreed.papyrusarabic.repository.iterators.MediaIterator
import com.robertreed.papyrusarabic.ui.ANIM_TO_NEXT
import com.robertreed.papyrusarabic.ui.ANIM_TO_PREV
import com.robertreed.papyrusarabic.ui.MainActivity
import com.robertreed.papyrusarabic.ui.MainViewModel

class LessonMediaFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var image: ImageView
    private lateinit var context: TextView
    private lateinit var header: TextView
    private lateinit var navLeft: ImageButton
    private lateinit var navRight: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var nextButton: ImageButton

    private lateinit var mediaPlayer : MediaPlayer

    private lateinit var mediaSet : MediaIterator

    private var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lesson_media, container, false)

        mediaSet = viewModel.getMedia()

        val pageLiveData = viewModel.currentPage()

        image = view.findViewById(R.id.image)
        context = view.findViewById(R.id.context)
        header = view.findViewById(R.id.header)

        navLeft = view.findViewById(R.id.nav_left)
        navLeft.setOnClickListener {
            viewModel.navToPrevPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_PREV)
            })
        }

        navRight = view.findViewById(R.id.nav_right)
        navRight.setOnClickListener {
            viewModel.navToNextPage()
            viewModel.currentPage().observe(viewLifecycleOwner, Observer {
                (requireActivity() as MainActivity).replacePage(viewLifecycleOwner, ANIM_TO_NEXT)
            })
        }

        prevButton = view.findViewById(R.id.prev_button)
        prevButton.isEnabled = false
        prevButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()

            nextButton.isEnabled = true

            index -= 1
            if(index == 0)
                prevButton.isEnabled = false

            loadMedia()
        }

        playButton = view.findViewById(R.id.play_button)
        playButton.isEnabled = false
        playButton.setOnClickListener {
            if(mediaPlayer.isPlaying)
                mediaPlayer.pause()
            mediaPlayer.seekTo(0)
            mediaPlayer.start()
        }

        nextButton = view.findViewById(R.id.next_button)
        nextButton.isEnabled = false
        nextButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()

            prevButton.isEnabled = true

            index += 1
            if(index == mediaSet.size() - 1)
                nextButton.isEnabled = false

            loadMedia()
        }

        pageLiveData.observe(viewLifecycleOwner, Observer { page ->
            if (viewModel.hasPageLoaded()) {
                pageLiveData.removeObservers(viewLifecycleOwner)
                context.text = page.number.toString()
            }
        })
        if(mediaSet.isLoaded()) {
            loadMedia()
            playButton.isEnabled = true
            nextButton.isEnabled = true
        } else {
            mediaSet.input.observe(viewLifecycleOwner, Observer {
                if(mediaSet.isLoaded()) {
                    mediaSet.input.removeObservers(viewLifecycleOwner)
                    loadMedia()
                    playButton.isEnabled = true
                    nextButton.isEnabled = true
                }
            })
        }

        return view
    }

    private fun loadMedia() {

        val currentData = mediaSet.get(index)
        header.text = currentData.name

        val imageResource
                = resources.getIdentifier(currentData.imageName, "drawable", requireActivity().packageName)
        image.setImageResource(imageResource)

        val soundResource
                = resources.getIdentifier(currentData.soundName,"raw", requireActivity().packageName)
        mediaPlayer = MediaPlayer.create(requireContext(), soundResource)
        mediaPlayer.start()
    }

    companion object {
        fun newInstance() = LessonMediaFragment()
    }
}
