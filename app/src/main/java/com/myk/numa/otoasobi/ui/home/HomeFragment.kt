package com.myk.numa.otoasobi.ui.home

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myk.numa.otoasobi.R
import com.myk.numa.otoasobi.databinding.FragmentHomeBinding
import com.myk.numa.otoasobi.repository.AudioManager

class HomeFragment : Fragment() {

    private lateinit var audioManager: AudioManager
    private val homeViewModel = HomeViewModel()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recorder: AudioRecord

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
            recorder.startRecording()
        }
        initAudioRecord()
        return binding.root
    }

    private val samplingRate = 44100
    private val samplingRateInHz = 4000

    private fun initAudioRecord() {
        /* TODO permission 許可 */
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, samplingRateInHz,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT, samplingRate
        )
        audioManager = AudioManager()

        val short = ShortArray(samplingRate / 2)

        recorder.setRecordPositionUpdateListener(object : AudioRecord.OnRecordPositionUpdateListener {
            override fun onMarkerReached(audioRecord: AudioRecord) {
            }

            override fun onPeriodicNotification(audioRecord: AudioRecord) {
                recorder.read(short, 0, samplingRate / 2)
                audioManager.createRecordingFile()
            }

        })
        recorder.positionNotificationPeriod = samplingRate / 2
    }

    override fun onDestroy() {
        super.onDestroy()
        recorder.release()
    }
}
