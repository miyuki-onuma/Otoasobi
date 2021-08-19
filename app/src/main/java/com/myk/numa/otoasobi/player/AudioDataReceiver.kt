package com.myk.numa.otoasobi.player

import java.nio.ByteBuffer

class AudioDataReceiver: MyAudioPlayer.AudioDataFetch {

    @Volatile
    private var isLocked = false
    private var audioDataListener: AudioDataListener? = null

    fun setAudioDataListener(audioDataListener: AudioDataListener?) {
        this.audioDataListener = audioDataListener
    }

    override fun setAudioDataAsByteBuffer(buffer: ByteBuffer?, sampleRate: Int, channelCount: Int) {
        //raw PCM data
        val shortBuffer = buffer?.asShortBuffer()
        val data = shortBuffer?.limit()?.let { ShortArray(it) }
        shortBuffer?.get(data)

        if (data == null) return
        //frames par sample
        val numFrames = data.size / channelCount
        val rawData = ShortArray(numFrames)
        var sum: Long = 0
        var value = 0

        //took average of all channel data
        for (i in 0 until numFrames) {
            sum = 0
            for (ch in 0 until channelCount) {
                value = (data[channelCount * i + ch] + 32768)
                sum += value.toLong()
            }
            rawData[i] = (sum / channelCount).toShort()
        }
        audioDataListener!!.setRawAudioBytes(rawData.clone())
        setLocked(false)
    }

    fun isLocked(): Boolean {
        return isLocked
    }

    fun setLocked(locked: Boolean) {
        isLocked = locked
    }

    interface AudioDataListener {
        fun setRawAudioBytes(shorts: ShortArray?)
    }

}
