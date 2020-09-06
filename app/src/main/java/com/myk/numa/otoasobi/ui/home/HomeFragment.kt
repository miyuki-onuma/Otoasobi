package com.myk.numa.otoasobi.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myk.numa.otoasobi.R
import com.myk.numa.otoasobi.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val homeViewModel = HomeViewModel()
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textHome.text = it
        })
        binding.audio.setOnClickListener {
            Toast.makeText(activity, R.string.start_record, Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

}
