package com.myk.numa.otoasobi.recorder

import android.content.Context
import android.util.Log
import com.myk.numa.otoasobi.util.parseRFC3339Date
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class AudioFile {

    companion object {
        private const val FILESIZE_SEEK = 4L
        private const val DATASIZE_SEEK = 40L
    }

    lateinit var file: File
    private lateinit var randomAccessFile: RandomAccessFile

    private val RIFF = byteArrayOf(
        'R'.toByte(),
        'I'.toByte(),
        'F'.toByte(),
        'F'.toByte()
    ) //wavファイルリフチャンクに書き込むチャンクID用

    private var fileSize = 36
    private val WAVE =
        byteArrayOf('W'.toByte(), 'A'.toByte(), 'V'.toByte(), 'E'.toByte()) //WAV形式でRIFFフォーマットを使用する

    private val fmt =
        byteArrayOf('f'.toByte(), 'm'.toByte(), 't'.toByte(), ' '.toByte()) //fmtチャンク　スペースも必要

    private val fmtSize = 16 //fmtチャンクのバイト数

    private val fmtID = byteArrayOf(1, 0) // フォーマットID リニアPCMの場合01 00 2byte

    private val chCount: Short = 1 //チャネルカウント モノラルなので1 ステレオなら2にする

    private val bytePerSec = Define.SAMPLING_RATE * (fmtSize / 8) * chCount //データ速度

    private val blockSize =
        (fmtSize / 8 * chCount).toShort() //ブロックサイズ (Byte/サンプリングレート * チャンネル数)

    private val bitPerSample: Short = 16 //サンプルあたりのビット数 WAVでは8bitか16ビットが選べる

    private val data =
        byteArrayOf('d'.toByte(), 'a'.toByte(), 't'.toByte(), 'a'.toByte()) //dataチャンク

    private var dataSize = 0 //波形データのバイト数

    init {
        createRecordingFile()
    }

    private fun createRecordingFile() {
        try {
            file = createNewEmptyFile()
            file.createNewFile()
            Log.e("AudioManager", "outputStream initialized")
            randomAccessFile = RandomAccessFile(file, "rw")

            //wavのヘッダを書き込み
            randomAccessFile.seek(0)
            randomAccessFile.write(RIFF)
            randomAccessFile.write(littleEndianInteger(fileSize))
            randomAccessFile.write(WAVE)
            randomAccessFile.write(fmt)
            randomAccessFile.write(littleEndianInteger(fmtSize))
            randomAccessFile.write(fmtID)
            randomAccessFile.write(littleEndianShort(chCount))
            randomAccessFile.write(littleEndianInteger(Define.SAMPLING_RATE)) //サンプリング周波数
            randomAccessFile.write(littleEndianInteger(bytePerSec))
            randomAccessFile.write(littleEndianShort(blockSize))
            randomAccessFile.write(littleEndianShort(bitPerSample))
            randomAccessFile.write(data)
            randomAccessFile.write(littleEndianInteger(dataSize))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun littleEndianInteger(i: Int): ByteArray {
        val buffer = ByteArray(4)
        buffer[0] = i.toByte()
        buffer[1] = (i shr 8).toByte()
        buffer[2] = (i shr 16).toByte()
        buffer[3] = (i shr 24).toByte()
        return buffer
    }

    // short型変数をリトルエンディアンのbyte配列に変更
    private fun littleEndianShort(s: Short): ByteArray? {
        val buffer = ByteArray(2)
        buffer[0] = s.toByte()
        buffer[1] = (s.toInt() shr 8).toByte()
        return buffer
    }

    // PCMデータを追記するメソッド
    fun writeByteToPcm(audioBuffer: ShortArray) = try {

        // ファイルにデータを追記
        randomAccessFile.seek(randomAccessFile.length())
        randomAccessFile.write(littleEndianShorts(audioBuffer))

        // ファイルサイズを更新
        updateFileSize()

        // データサイズを更新
        updateDataSize()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun littleEndianShorts(s: ShortArray): ByteArray {
        val buffer = ByteArray(s.size * 2)
        s.forEachIndexed { i, sh ->
            buffer[i * 2] = s[i].toByte()
            buffer[i * 2 + 1] = (s[i].toInt() shr 8).toByte()
        }
        return buffer
    }

    // ファイルサイズを更新
    private fun updateFileSize() = try {
        fileSize = (file.length() - 8).toInt()
        val byte = littleEndianInteger(fileSize)
        randomAccessFile.seek(FILESIZE_SEEK)
        randomAccessFile.write(byte)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    // データサイズを更新
    private fun updateDataSize() = try {
        dataSize = (file.length() - 44).toInt()
        val byte = littleEndianInteger(dataSize)
        randomAccessFile.seek(DATASIZE_SEEK)
        randomAccessFile.write(byte)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun createNewEmptyFile(): File {
        return File(Define.FILEPATH, getFileName())
    }

    private fun getFileName(): String {
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = df.format(Date())
        return date.parseRFC3339Date().toString() + ".wav"
    }
}
