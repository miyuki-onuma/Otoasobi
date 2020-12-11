package com.myk.numa.otoasobi

import android.content.Context
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.audio.*
import com.google.android.exoplayer2.audio.DefaultAudioSink.AudioProcessorChain
import com.google.android.exoplayer2.audio.TeeAudioProcessor.AudioBufferSink

class CustomRenderersFactory(context: Context, private val listener: AudioBufferSink) :
    DefaultRenderersFactory(context), AudioProcessorChain {
    private var teeAudioProcessor: TeeAudioProcessor = TeeAudioProcessor(listener)

    override fun buildAudioSink(
        context: Context,
        enableFloatOutput: Boolean,
        enableAudioTrackPlaybackParams: Boolean,
        enableOffload: Boolean
    ): AudioSink? {
        return DefaultAudioSink(
            AudioCapabilities.getCapabilities(context),
            this,
            enableFloatOutput,
            enableAudioTrackPlaybackParams,
            enableOffload
        )
    }

    override fun getAudioProcessors(): Array<AudioProcessor> =
        //return audioProcessor with custom teeAudioProcessor
        arrayOf(teeAudioProcessor)

    override fun applyPlaybackParameters(playbackParameters: PlaybackParameters): PlaybackParameters = playbackParameters

    override fun applySkipSilenceEnabled(skipSilenceEnabled: Boolean): Boolean = skipSilenceEnabled

    override fun getMediaDuration(playoutDuration: Long): Long = playoutDuration

    override fun getSkippedOutputFrameCount(): Long = 0
}
