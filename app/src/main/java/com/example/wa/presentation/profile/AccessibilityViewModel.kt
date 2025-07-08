package com.example.wa.presentation.profile

import android.app.Application
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class AccessibilityViewModel(application: Application) : AndroidViewModel(application) {

    private val context = application.applicationContext
    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)
    val isTtsReady: StateFlow<Boolean> = _isTtsReady

    fun initTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.getDefault())
                _isTtsReady.value = result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
            } else {
                _isTtsReady.value = false
            }
        }
    }

    fun speak(text: String) {
        if (_isTtsReady.value) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        } else {
            Toast.makeText(context, "Sintesi vocale non disponibile", Toast.LENGTH_SHORT).show()
        }
    }

    fun releaseTTS() {
        tts?.stop()
        tts?.shutdown()
    }
}
