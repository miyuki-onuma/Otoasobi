package com.myk.numa.otoasobi.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myk.numa.otoasobi.databinding.FragmentTimelineBinding
import com.myk.numa.otoasobi.player.MyAudioPlayer
import com.myk.numa.otoasobi.recorder.Define
import com.myk.numa.otoasobi.ui.core.AppSharePreference

class TimeLineFragment : Fragment() {

    private var preferences: AppSharePreference? = null

    private val player = MyAudioPlayer()

    private val adapter = TimeLineAdapter(
        onClickItemTimeLineListener = { voice ->
            context?.let {
                player.play(it, voice.path)
            }
        }
    )
    private var viewModel = TimeLineViewModel()
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
        preferences = AppSharePreference(requireContext(), generateGson())
        val list = preferences?.getStringList(Define.KEY_VOICE) ?: emptyList()
        adapter.addAllData(list)
    }
    private fun generateGson(): Gson {
        return GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }

            override fun shouldSkipField(f: FieldAttributes?): Boolean {
                return false
            }
        }).create()
    }


}