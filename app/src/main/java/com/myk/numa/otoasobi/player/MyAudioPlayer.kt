package com.myk.numa.otoasobi.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.analytics.AnalyticsListener
import com.google.android.exoplayer2.audio.AudioListener
import com.google.android.exoplayer2.audio.TeeAudioProcessor
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.video.VideoListener
import com.myk.numa.otoasobi.CustomRenderersFactory
import java.nio.ByteBuffer

class MyAudioPlayer: TeeAudioProcessor.AudioBufferSink {

    private var player: SimpleExoPlayer? = null

    fun play(context: Context, uriString: String) {
        val dataSourceFactory = DefaultDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(Uri.encode(uriString))
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)
        player?.setAudioAttributes(com.google.android.exoplayer2.audio.AudioAttributes.DEFAULT, true)
        player?.setMediaSource(mediaSource)
        player?.prepare()
        player?.play()
    }

    fun initializePlayer(context: Context) {
        val player = SimpleExoPlayer.Builder(context, CustomRenderersFactory(context, this)).build()

        player.addListener(object : Player.EventListener {

            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) = Unit

            override fun onSeekProcessed() = Unit

            override fun onTracksChanged(
                trackGroups: TrackGroupArray,
                trackSelections: TrackSelectionArray
            ) = Unit

            override fun onIsLoadingChanged(isLoading: Boolean) = Unit

            override fun onPlayerError(error: ExoPlaybackException) = Unit

            override fun onPlayWhenReadyChanged(playWhenReady: Boolean, reason: Int) = Unit

            override fun onLoadingChanged(isLoading: Boolean) = Unit

            override fun onPositionDiscontinuity(reason: Int) = Unit

            override fun onRepeatModeChanged(repeatMode: Int) = Unit

            override fun onPlaybackStateChanged(state: Int) = Unit

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) = Unit

            override fun onPlaybackSuppressionReasonChanged(playbackSuppressionReason: Int) = Unit

            override fun onTimelineChanged(timeline: Timeline, reason: Int) = Unit

            override fun onTimelineChanged(timeline: Timeline, manifest: Any?, reason: Int) = Unit

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) = Unit

            override fun onIsPlayingChanged(isPlaying: Boolean) = Unit

            override fun onExperimentalOffloadSchedulingEnabledChanged(
                offloadSchedulingEnabled: Boolean
            ) = Unit

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) = Unit

        })

        player.addAnalyticsListener(object : AnalyticsListener {})

        player.addVideoListener(object : VideoListener {})

        player.addAudioListener(object : AudioListener {})

        player.addMetadataOutput { metadata -> }

        player.addTextOutput { cue -> }

        this.player = player
    }

    fun release() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun flush(sampleRateHz: Int, channelCount: Int, encoding: Int) {
        TODO("Not yet implemented")
    }

    override fun handleBuffer(buffer: ByteBuffer) {
        TODO("Not yet implemented")
    }
}
