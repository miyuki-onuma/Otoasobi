package com.myk.numa.otoasobi

import com.myk.numa.otoasobi.di.appModule
import com.myk.numa.otoasobi.di.netModule
import com.myk.numa.otoasobi.di.viewModelModule
import net.danlew.android.joda.JodaTimeAndroid
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()

        // Koinコンテナ生成
        startKoin {
            androidLogger()
            androidContext(this@Application)
            modules(listOf(appModule, netModule, viewModelModule))
        }


        JodaTimeAndroid.init(this)
    }

}