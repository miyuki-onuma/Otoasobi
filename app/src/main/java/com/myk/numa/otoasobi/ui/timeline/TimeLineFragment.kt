package com.myk.numa.otoasobi.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.myk.numa.otoasobi.databinding.FragmentTimelineBinding

class TimeLineFragment : Fragment() {

    private val adapter = TimeLineAdapter(
        onClickItemTimeLineListener = {
            context?.let {
                // TODO
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

        binding.list.adapter = adapter

        return binding.root
    }
}
