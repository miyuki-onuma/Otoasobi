package com.myk.numa.otoasobi.player

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.media.AudioTrack.OnPlaybackPositionUpdateListener
import android.os.Build
import androidx.annotation.RequiresApi
import com.myk.numa.otoasobi.recorder.Define
import java.io.File
import java.io.FileInputStream


@RequiresApi(Build.VERSION_CODES.M)
class MyAndroidPlayer {

    private var audioTrack: AudioTrack? = null
    private val playByteData = ByteArray(4096)
    private val playShortData = ShortArray(2048)
    private lateinit var playStream: FileInputStream

    fun play() = try {
        // 再生中の場合は止める
        if (audioTrack != null && audioTrack?.state == AudioTrack.PLAYSTATE_PLAYING) {
            audioTrack?.stop()
        } else {
            playInit()
            val file = File(Define.FILEPATH)

            playStream = FileInputStream(file)
            for (i in 0..2) {
                playStream.read(playByteData)
                byte2short(playShortData, playByteData)
                // データを再生バッファに書き込む
                audioTrack?.write(playShortData, 0, playByteData.size)
            }

            // playStateを再生状態(PLAYSTATE_PLAYING)にする
            audioTrack?.play()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun playInit() {
        val playBufSize = AudioTrack.getMinBufferSize(
            Define.SAMPLING_RATE,  //バッファサイズ
            AudioFormat.CHANNEL_OUT_MONO,  //チャンネル
            AudioFormat.ENCODING_PCM_16BIT) //エンコーディング形式 pcm16ビット
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
            .setAudioFormat(AudioFormat.Builder()
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setSampleRate(Define.SAMPLING_RATE)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build())
            .setBufferSizeInBytes(playBufSize)
            .setTransferMode(AudioTrack.MODE_STATIC)
            .build()
        // 音声データをいくつずつ処理するか( = 1フレームのデータ数)
        audioTrack?.positionNotificationPeriod = Define.SAMPLING_RATE / 2

        // 1回分の再生終了を検知して停止する
        audioTrack?.setPlaybackPositionUpdateListener(object: OnPlaybackPositionUpdateListener {
            override fun onMarkerReached(track: AudioTrack) {
                //再生完了
                if(track.playState == AudioTrack.PLAYSTATE_PLAYING){
                    //停止
                    track.stop()
                }
            }

            override fun onPeriodicNotification(track: AudioTrack) {
                try {
                    // 最後まで再生済みの場合−１になる
                    val endBuf = playStream.read(playByteData)
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
    fun byte2short(data: ShortArray, byteArray: ByteArray){
        for(i in 0 until byteArray.size / 2) {
            data[i] = (byteArray[2 * i].toShort() + byteArray[2 * i + 1].toShort() * 256).toShort()
        }
    }
}
