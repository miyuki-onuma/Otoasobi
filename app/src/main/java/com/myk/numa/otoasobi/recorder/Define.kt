package com.myk.numa.otoasobi.recorder

import android.media.AudioFormat

class Define {

    companion object {
        const val KEY_VOICE = "KEY_VOICE"
        const val KEY_CURRENT_VOICE = "KEY_CURRENT_VOICE"
        const val FILEPATH = "data/data/com.myk.numa.otoasobi/"
        const val SAMPLING_RATE = 44100
        const val CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO

        const val CHANNEL_OUT = AudioFormat.CHANNEL_OUT_MONO
        //エンコーディング形式 pcm16ビット
        const val ENCODE_FORMAT = AudioFormat.ENCODING_PCM_16BIT
    }

}
