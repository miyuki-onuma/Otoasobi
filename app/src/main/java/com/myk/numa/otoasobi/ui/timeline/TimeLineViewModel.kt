package com.myk.numa.otoasobi.ui.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myk.numa.otoasobi.recorder.Define
import com.myk.numa.otoasobi.ui.core.AppSharePreference
import org.koin.core.KoinComponent
import org.koin.core.inject

class TimeLineViewModel : ViewModel(), KoinComponent {

    private val preferences: AppSharePreference by inject()

    fun getVoiceList() : List<String> {
        return preferences.getStringList(Define.KEY_VOICE) ?: emptyList()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}
