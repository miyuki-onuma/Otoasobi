package com.myk.numa.otoasobi

import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.audio.AudioCapabilities
import com.google.android.exoplayer2.audio.AudioSink
import com.google.android.exoplayer2.audio.DefaultAudioSink
import com.google.android.exoplayer2.audio.TeeAudioProcessor

class CustomRenderersFactory(context: Context, private val listener: TeeAudioProcessor.AudioBufferSink) :
    DefaultRenderersFactory(context) {

    override fun buildAudioSink(
        context: Context,
        enableFloatOutput: Boolean,
        enableAudioTrackPlaybackParams: Boolean,
        enableOffload: Boolean
    ): AudioSink? {
        return DefaultAudioSink(
            AudioCapabilities.DEFAULT_AUDIO_CAPABILITIES,
            DefaultAudioSink.DefaultAudioProcessorChain(TeeAudioProcessor(listener)).audioProcessors
        )
    }
}