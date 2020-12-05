package com.myk.numa.otoasobi.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myk.numa.otoasobi.recorder.Define
import com.myk.numa.otoasobi.ui.core.AppSharePreference
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {


    val preferences: AppSharePreference by inject()

    fun saveVoice(path: String) {
        preferences.putStringList(Define.KEY_VOICE, path)
    }


    private val _text = MutableLiveData<String>().apply {
        value = "おとであそぼう"
    }
    val text: LiveData<String> = _text
}
