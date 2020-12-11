package com.myk.numa.otoasobi.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myk.numa.otoasobi.R
import com.myk.numa.otoasobi.databinding.FragmentHomeBinding
import com.myk.numa.otoasobi.player.MyAudioPlayer
import com.myk.numa.otoasobi.recorder.MyAudioRecorder
import com.myk.numa.otoasobi.util.DialogUtils
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent

class HomeFragment : Fragment(), KoinComponent {

    private val PERMISSION_REQUEST_CODE = 0
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private lateinit var binding: FragmentHomeBinding
    private var recorder: MyAudioRecorder? = null
    private lateinit var player: MyAudioPlayer
    private var latestPath: String? = null

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

        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
        player = MyAudioPlayer()
        binding.playBtn.apply {
            if (latestPath == null) isEnabled = false
            setOnClickListener {
                player.play(requireContext(), latestPath!!)
            }
        }
        binding.startRecordBtn.setOnClickListener {
            if (recorder == null) return@setOnClickListener
            if (recorder!!.isRecording()) {
                Toast.makeText(activity, R.string.stop_record, Toast.LENGTH_SHORT).show()
                recorder?.stopRecord {
                    homeViewModel.saveVoice(it.path)
                    latestPath = it.path
                    playBtn.isEnabled = true
                    DialogUtils.dialogShowMessage(
                        requireContext(),
                        message = getString(R.string.stop_record),
                        textPositive = getString(R.string.ok)
                    )
                }
            } else {
                Toast.makeText(activity, R.string.start_record, Toast.LENGTH_SHORT).show()
                recorder?.startRecord()
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        player.initializePlayer(requireContext())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            recorder = MyAudioRecorder()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder?.release()
        player.release()
    }
}
