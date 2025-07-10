package com.example.wa.presentation.profile

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

object TTSHelper {
    private var tts: TextToSpeech? = null
    private var isReady = false
    private var isEnabled = false
    private var appContext: Context? = null

    fun init(application: Application) {
        appContext = application.applicationContext

        if (isEnabled) {
            initTTS()
        }
    }

    private fun initTTS() {
        if (tts == null && appContext != null) {
            tts = TextToSpeech(appContext!!) { status ->
                isReady = status == TextToSpeech.SUCCESS
                if (isReady) {
                    tts?.speak("TTS inizializzato correttamente", TextToSpeech.QUEUE_FLUSH, null, "test_init")
                }
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        isEnabled = enabled
        appContext?.let { context ->
            val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("tts_enabled", enabled).apply()
            if (enabled) {
                initTTS()
            } else {
                tts?.shutdown()
                tts = null
                isReady = false
            }
        }
    }

    fun speak(text: String) {
        if (isEnabled && isReady && text.isNotEmpty()) {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speak_$text")
        }
    }

    fun isEnabled(): Boolean {
        return isEnabled
    }

}

fun View.enableAutoTTS() {

    if (!TTSHelper.isEnabled()) {
        return
    }

    if (this is TextView && text.isNotEmpty()) {

        isClickable = true
        isFocusable = true

        setOnClickListener {
            TTSHelper.speak(text.toString())
        }

    }

    if (this is RecyclerView) {
        return
    }

    if (this is ViewGroup) {
        for (i in 0 until childCount) {
            getChildAt(i).enableAutoTTS()
        }
    }
}

fun Fragment.enableTTS() {
    view?.enableAutoTTS()
}

fun RecyclerView.ViewHolder.enableTTS() {
    if (TTSHelper.isEnabled()) {
        itemView.enableAutoTTS()
    }
}