package com.myk.numa.otoasobi.repository

import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class AudioManager {

    private lateinit var outputStream: BufferedOutputStream
    private val filePath = "/sdcard/test.wav"

    fun createRecordingFile() {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
        try {
            file.createNewFile()
            outputStream = BufferedOutputStream(FileOutputStream(file))
            Log.e("AudioManager", "outputStream initialized")

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }
}
