package com.myk.numa.otoasobi.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myk.numa.otoasobi.databinding.FragmentTimelineBinding
import com.myk.numa.otoasobi.player.MyAudioPlayer
import com.myk.numa.otoasobi.recorder.Define
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class TimeLineFragment : Fragment(), KoinComponent {

    private val player = MyAudioPlayer()
    private val adapter = TimeLineAdapter(
        onClickItemTimeLineListener = { voice ->
            context?.let {
                player.play(it, voice.path)
            }
        }
    )
    private val viewModel: TimeLineViewModel by sharedViewModel()
    private lateinit var binding: FragmentTimelineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        player.initializePlayer(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.adapter = adapter

        val list = viewModel.getVoiceList()
        adapter.addAllData(list)
    }

}