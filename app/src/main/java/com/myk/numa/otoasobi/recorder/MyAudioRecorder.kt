package com.myk.numa.otoasobi.recorder

import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import com.myk.numa.otoasobi.recorder.Define.Companion.CHANNEL_IN
import com.myk.numa.otoasobi.recorder.Define.Companion.ENCODE_FORMAT
import com.myk.numa.otoasobi.recorder.Define.Companion.SAMPLING_RATE

class MyAudioRecorder {

    private var audioBuffer: ShortArray
    private var audioFile: AudioFile
    private var recorder: AudioRecord
    private var status = Status.IS_READY
    private var recorderThread: Thread? = null

    enum class Status {
        IS_READY, RECORDING, STOP
    }

    init {
        status = Status.IS_READY
        Process.setThreadPriority(Process.THREAD_PRIORITY_AUDIO)

        var bufferSize = AudioRecord.getMinBufferSize(SAMPLING_RATE, CHANNEL_IN, ENCODE_FORMAT)
        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
            bufferSize = SAMPLING_RATE * 2
        }
        recorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, //通話マイクからの音声を録音
            SAMPLING_RATE, CHANNEL_IN, ENCODE_FORMAT, bufferSize
        )
        audioFile = AudioFile()

        audioBuffer = ShortArray(bufferSize / 2)
        recorder.setRecordPositionUpdateListener(object :
            AudioRecord.OnRecordPositionUpdateListener {
            override fun onMarkerReached(audioRecord: AudioRecord) {
            }

            override fun onPeriodicNotification(audioRecord: AudioRecord) {
            }
        })
    }

    fun isRecording() = status == Status.RECORDING

    fun startRecord() {
        if (recorderThread != null) return
        status = Status.RECORDING
        recorder.startRecording()
        recorderThread = Thread {
            while (status == Status.RECORDING) {
                // Short型Arrayにデータを読み込み
                recorder.read(audioBuffer, 0, audioBuffer.size)
                audioFile.writeByteToPcm(audioBuffer)
            }
            recorder.stop()
        }
        recorderThread?.start()
    }

    fun stopRecord() {
        status = Status.STOP
        recorderThread = null
    }

    fun release() {
        recorder.release()
    }
}
