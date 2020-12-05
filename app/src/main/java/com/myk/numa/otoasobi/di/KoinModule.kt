package com.myk.numa.otoasobi.di

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.myk.numa.otoasobi.Application
import com.myk.numa.otoasobi.ui.core.AppSharePreference
import com.myk.numa.otoasobi.ui.home.HomeViewModel
import com.myk.numa.otoasobi.ui.timeline.TimeLineViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        HomeViewModel()
    }

    viewModel {
        TimeLineViewModel()
    }
}

val appModule = module {
    single { androidApplication() as Application }
    single { AppSharePreference(get(), get()) }
}

val netModule = module {

    single {
        generalGSon()
    }
}

fun generalGSon(): Gson {
    return GsonBuilder().addSerializationExclusionStrategy(object : ExclusionStrategy {

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            return false
        }

        override fun shouldSkipField(f: FieldAttributes?): Boolean {
            return false
        }
    }).create()
}

