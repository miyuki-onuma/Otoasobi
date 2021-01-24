package com.myk.numa.otoasobi.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myk.numa.otoasobi.recorder.Define
import com.myk.numa.otoasobi.ui.core.AppSharePreference
import org.koin.core.KoinComponent
import org.koin.core.inject

class SettingsViewModel : ViewModel(), KoinComponent {

    private val preferences: AppSharePreference by inject()

    fun getCurrent() : String? {
        return preferences[Define.KEY_CURRENT_VOICE, String::class.java]
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is Settings Fragment"
    }
    val text: LiveData<String> = _text
}
