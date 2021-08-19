package com.myk.numa.otoasobi

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arthenica.mobileffmpeg.FFmpeg

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logFfmpeg()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_timeline, R.id.navigation_settings))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun logFfmpeg() {
        // FFmpegのバージョンを確認する
        FFmpeg.execute("-version")
        val rc = FFmpeg.getLastReturnCode()
        val output = FFmpeg.getLastCommandOutput()

        when (rc) {
            FFmpeg.RETURN_CODE_SUCCESS -> {
                Log.i("FFmpeg", "Success!")
            }
            FFmpeg.RETURN_CODE_CANCEL -> {
                Log.e("FFmpeg", output)
            }
            else -> {
                Log.e("FFmpeg", output)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (fragment in supportFragmentManager.fragments) {
            for(childFragment in fragment.childFragmentManager.fragments) {
                childFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }
}
