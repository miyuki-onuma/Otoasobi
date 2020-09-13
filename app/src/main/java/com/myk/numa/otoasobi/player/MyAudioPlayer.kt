package com.myk.numa.otoasobi.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioTrack.OnPlaybackPositionUpdateListener
import android.os.Build
import android.os.Process
import androidx.annotation.RequiresApi
import com.myk.numa.otoasobi.recorder.Define
import java.io.File
import java.io.FileInputStream


@RequiresApi(Build.VERSION_CODES.M)
class MyAudioPlayer {

    private var audioTrack: AudioTrack? = null
    private val playByteData = ByteArray(4096)
    private val playShortData = ShortArray(2048)
    private lateinit var playStream: FileInputStream

    fun play() = try {
        if (audioTrack != null && audioTrack?.state == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack?.stop()
        } else {
            playInit()
            val file = File(Define.FILEPATH)

            playStream = FileInputStream(file)
            for (i in 0..2) {
                playStream.read(playByteData)
                byte2short(playShortData, playByteData)
                audioTrack?.write(playShortData, 0, playByteData.size)
            }

            audioTrack?.play()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun playInit() {
        val playBufSize = AudioTrack.getMinBufferSize(
            Define.SAMPLING_RATE,  //バッファサイズ
            Define.CHANNEL_OUT,  //チャンネル
            Define.ENCODE_FORMAT)
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(Define.ENCODE_FORMAT)
                .setSampleRate(Define.SAMPLING_RATE)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build())
            .setBufferSizeInBytes(playBufSize)
            .build()
        audioTrack?.positionNotificationPeriod = Define.SAMPLING_RATE / 2
        audioTrack?.setPlaybackPositionUpdateListener(object: OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(track: AudioTrack) {
                audioTrack?.stop()
            }

            override fun onPeriodicNotification(track: AudioTrack) {
                try {
                    val endBuf = playStream.read(playByteData) //最後まで再生済みの場合−１になる
                    if (endBuf != -1) {
                        byte2short(playShortData, playByteData)
                        audioTrack?.write(playShortData, 0, playByteData.size)
                    } else {
                        audioTrack?.stop()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }

    //byteデータ列をshortのオーディオサンプル列に変換
    fun byte2short(data: ShortArray, bdata: ByteArray){
        for(i in 0 until bdata.size / 2) {
            // リトルエンディアン
            data[i] = (bdata[2 * i].toShort() + bdata[2 * i + 1].toShort() * 256).toShort()
        }
    }
}
