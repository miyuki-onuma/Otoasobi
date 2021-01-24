package com.myk.numa.otoasobi.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.arthenica.mobileffmpeg.FFmpeg
import com.google.android.material.slider.RangeSlider
import com.myk.numa.otoasobi.R
import com.myk.numa.otoasobi.databinding.FragmentSettingsBinding
import com.myk.numa.otoasobi.player.MyAudioPlayer
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent
import java.util.*


class SettingsFragment : Fragment(), KoinComponent {

    private val player = MyAudioPlayer()
    private val viewModel: SettingsViewModel by sharedViewModel()

    private lateinit var binding: FragmentSettingsBinding
    private var maxVolume: Double? = null
    private var meanVolume: Double? = null
    private var dB: Double = -10.0
    private var low: Int = 3000
    private var high: Int = 200

    private var thresholdValue = "-20dB"
    private var makeupValue = "3"
    private var ratioValue = "8"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
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

    val listener = RangeSlider.OnChangeListener { slider, value, fromUser ->
        binding.threshold.text = getString(R.string.db, value)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCurrent()?.let { path ->
            logFfmpeg(path)

            // 初期値
            binding.gain.text = getString(R.string.db, dB.toInt())
            binding.txtHighpass.text = getString(R.string.hlz, high)
            binding.txtLowpass.text = getString(R.string.hlz, low)

            // Gain (音量)
            binding.plusGain.setOnClickListener {
                dB++
                binding.gain.text = getString(R.string.db, dB.toInt())
            }
            binding.minusGain.setOnClickListener {
                dB--
                binding.gain.text = getString(R.string.db, dB.toInt())
            }

            // highpass/ lowpass filter
            binding.highpassPlus.setOnClickListener {
                high += 100
                binding.txtHighpass.text = getString(R.string.hlz, high)
            }
            binding.highpassMinus.setOnClickListener {
                high -= 100
                binding.txtHighpass.text = getString(R.string.hlz, high)
            }
            binding.lowpassPlus.setOnClickListener {
                low += 100
                binding.txtLowpass.text = getString(R.string.hlz, low)
            }
            binding.lowpassMinus.setOnClickListener {
                low -= 100
                binding.txtLowpass.text = getString(R.string.hlz, low)
            }


            // コンプレッサー
            binding.slThreshold.addOnChangeListener { slider, value, fromUser ->

                binding.threshold.text = getString(R.string.slider_db, value)
            }
            binding.slRatio.addOnChangeListener { slider, value, fromUser ->
                ratioValue = value.toString()
                binding.ratio.text = getString(R.string.ratio, value)
            }
            binding.slMakeup.addOnChangeListener { slider, value, fromUser ->
                binding.makeup.text = getString(R.string.slider_db, value)
            }



            binding.outputGain.setOnClickListener {
                gain(path)
            }
            binding.outputFilter.setOnClickListener {
                filter(path)
            }
            binding.outputCompressor.setOnClickListener {
                compressor(path)
            }

            binding.play.setOnClickListener {
                play()
            }

        } ?: kotlin.run {
            binding.textSettings.text ="Timeline で選択してください"
        }

    }

    private fun play() {
        context?.let { player.play(it, outputPath) }
    }

    var outputPath = ""

    private fun gain(filename: String) {
        val uuid = UUID.randomUUID()
        outputPath = "$filename$uuid.wav"
        val dbString = dB.toString() + "dB"
        FFmpeg.execute("-i $filename -af volume=$dbString $outputPath")
        val rc = FFmpeg.getLastReturnCode()
        val output = FFmpeg.getLastCommandOutput()
        when (rc) {
            FFmpeg.RETURN_CODE_SUCCESS -> {
                Toast.makeText(context, "FFmpeg Success!\n $output", Toast.LENGTH_LONG).show()
                Log.d("Otoasobi:", "FFmpeg Success!\n $output")
            }
            FFmpeg.RETURN_CODE_CANCEL -> {
                Toast.makeText(context, "FFmpeg Cancel\n $output", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "FFmpeg Error\n $output", Toast.LENGTH_LONG).show()
            }
        }

        logFfmpeg(outputPath)
    }


    private fun filter(filename: String) {
        val uuid = UUID.randomUUID()
        outputPath = "$filename$uuid.wav"
        FFmpeg.execute("-i $filename -af highpass=f=$high,lowpass=f=$low $outputPath")
        val rc = FFmpeg.getLastReturnCode()
        val output = FFmpeg.getLastCommandOutput()
        when (rc) {
            FFmpeg.RETURN_CODE_SUCCESS -> {
                Toast.makeText(context, "FFmpeg Success!\n $output", Toast.LENGTH_LONG).show()
                Log.d("Otoasobi:", "FFmpeg Success!\n $output")
            }
            FFmpeg.RETURN_CODE_CANCEL -> {
                Toast.makeText(context, "FFmpeg Cancel\n $output", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "FFmpeg Error\n $output", Toast.LENGTH_LONG).show()
            }
        }

        logFfmpeg(outputPath)
    }

    private fun compressor(filename: String) {
        val uuid = UUID.randomUUID()
        outputPath = "$filename$uuid.wav"
        FFmpeg.execute("-i $filename -af acompressor=threshold=${thresholdValue}:ratio=${ratioValue}:makeup=${makeupValue}:attack=5:release=100 $outputPath")
        val rc = FFmpeg.getLastReturnCode()
        val output = FFmpeg.getLastCommandOutput()
        when (rc) {
            FFmpeg.RETURN_CODE_SUCCESS -> {
                Toast.makeText(context, "FFmpeg Success!\n $output", Toast.LENGTH_LONG).show()
                Log.d("Otoasobi:", "FFmpeg Success!\n $output")
            }
            FFmpeg.RETURN_CODE_CANCEL -> {
                Toast.makeText(context, "FFmpeg Cancel\n $output", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "FFmpeg Error\n $output", Toast.LENGTH_LONG).show()
            }
        }

        logFfmpeg(outputPath)
    }


    @SuppressLint("SetTextI18n")
    private fun logFfmpeg(filename: String) {
        binding.textSettings.text = filename
        FFmpeg.execute("-i $filename -af volumedetect -f null NULL")

        val output = FFmpeg.getLastCommandOutput()

        val DELIMITER_TIME = "time="
        val DELIMITER_SPACE = " "
        val DELIMITER_MEAN_VOLUME = "mean_volume: "
        val DELIMITER_MAX_VOLUME = "max_volume: "

        // NOTE: time抽出
        var resultTime = ""
        if (Regex(DELIMITER_TIME).containsMatchIn(output)) {
            val times = output.split(DELIMITER_TIME)
            // NOTE: timeが２つあるが２つ目が音声ファイルの長さを表す。
            resultTime = if (times.size == 3) {
                times[2].split(DELIMITER_SPACE)[0]
            } else {
                times[1].split(DELIMITER_SPACE)[0]
            }
        }
        Log.e("Otoasobi:", "time抽出: $resultTime")
        binding.textSettings.text = binding.textSettings.text.toString() + "\n" + "time: $resultTime"

        // NOTE: mean_volume抽出
        if (Regex(DELIMITER_MEAN_VOLUME).containsMatchIn(output)) {
            meanVolume = try {
                output.split(DELIMITER_MEAN_VOLUME)[1].split(DELIMITER_SPACE)[0].toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }
        Log.e("Otoasobi:", "mean_volume抽出: $meanVolume")
        binding.textSettings.text = binding.textSettings.text.toString() + "\n" + "mean_volume: $meanVolume"

        // NOTE: max_volume抽出
        if (Regex(DELIMITER_MAX_VOLUME).containsMatchIn(output)) {
            maxVolume = try {
                output.split(DELIMITER_MAX_VOLUME)[1].split(DELIMITER_SPACE)[0].toDouble()
            } catch (e: NumberFormatException) {
                null
            }
        }
        Log.e("Otoasobi:", "max_volume抽出: $maxVolume")
        binding.textSettings.text = binding.textSettings.text.toString() + "\n" + "max_volume: $maxVolume"
    }
}
