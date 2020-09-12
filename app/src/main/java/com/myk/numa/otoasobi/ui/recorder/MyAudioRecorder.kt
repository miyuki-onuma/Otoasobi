package com.myk.numa.otoasobi.ui.recorder

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import com.myk.numa.otoasobi.repository.AudioFile

class MyAudioRecorder {

    private var audioFile: AudioFile
    private var recorder: AudioRecord
    private var status = Status.IS_READY

    private val samplingRate = 44100
    private val samplingRateInHz = 4000

    enum class Status {
        IS_READY, RECORDING, STOP
    }

    init {
        status = Status.IS_READY
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, //通話マイクからの音声を録音
            samplingRateInHz,
            AudioFormat.CHANNEL_IN_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            samplingRate
        )
        audioFile = AudioFile()

        recorder.setRecordPositionUpdateListener(object : AudioRecord.OnRecordPositionUpdateListener {
            override fun onMarkerReached(audioRecord: AudioRecord) {
            }

            override fun onPeriodicNotification(audioRecord: AudioRecord) {
                // 録音データ読み込み
                val audioBuffer = ShortArray(samplingRate / 2)
                // Short型Arrayにデータを読み込み
                recorder.read(audioBuffer, 0, samplingRate / 2)
                audioFile.writeByteToPcm(audioBuffer)
            }

        })
        // コールバック間隔を指定
        recorder.positionNotificationPeriod = samplingRate / 2
    }

    fun isRecording() = status == Status.RECORDING

    fun startRecord() {
        status = Status.RECORDING
        recorder.startRecording()

        Thread {
            // 録音データ読み込み
            val audioBuffer = ShortArray(samplingRate / 2)
            // Short型Arrayにデータを読み込み
            recorder.read(audioBuffer, 0, samplingRate / 2)
        }
    }

    fun stopRecord() {
        status = Status.STOP
        recorder.stop()
    }

    fun release() {
        recorder.release()
    }
}
