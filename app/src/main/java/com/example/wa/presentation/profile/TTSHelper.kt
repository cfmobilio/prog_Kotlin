// TTSHelper.kt - Versione con debug per trovare il problema
package com.example.wa.presentation.profile

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import java.util.*

object TTSHelper {
    private const val TAG = "TTSHelper"
    private var tts: TextToSpeech? = null
    private var isReady = false
    private var isEnabled = false
    private var appContext: Context? = null

    fun init(application: Application) {
        Log.d(TAG, "🔧 Inizializzazione TTSHelper...")
        appContext = application.applicationContext

        val prefs = appContext!!.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        isEnabled = prefs.getBoolean("tts_enabled", false)

        Log.d(TAG, "📋 TTS abilitato dalle preferenze: $isEnabled")

        if (isEnabled) {
            initTTS()
        } else {
            Log.w(TAG, "⚠️ TTS disabilitato nelle preferenze - attivalo dal profilo!")
        }
    }

    private fun initTTS() {
        Log.d(TAG, "🎤 Inizializzazione TextToSpeech...")
        if (tts == null && appContext != null) {
            tts = TextToSpeech(appContext!!) { status ->
                Log.d(TAG, "📱 Callback TTS - Status: $status")
                isReady = status == TextToSpeech.SUCCESS
                if (isReady) {
                    val result = tts?.setLanguage(Locale.getDefault())
                    Log.d(TAG, "✅ TTS pronto! Lingua impostata: ${Locale.getDefault()}, Result: $result")

                    // Test immediato
                    tts?.speak("TTS inizializzato correttamente", TextToSpeech.QUEUE_FLUSH, null, "test_init")
                } else {
                    Log.e(TAG, "❌ Errore inizializzazione TTS!")
                }
            }
        }
    }

    fun setEnabled(enabled: Boolean) {
        Log.d(TAG, "🔄 Cambio stato TTS: $enabled")
        isEnabled = enabled
        appContext?.let { context ->
            val prefs = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            prefs.edit().putBoolean("tts_enabled", enabled).apply()
            Log.d(TAG, "💾 Preferenza salvata: tts_enabled = $enabled")

            if (enabled) {
                initTTS()
            } else {
                tts?.shutdown()
                tts = null
                isReady = false
                Log.d(TAG, "🔇 TTS spento")
            }
        }
    }

    fun speak(text: String) {
        Log.d(TAG, "🗣️ Tentativo di parlare: '$text'")
        Log.d(TAG, "   - isEnabled: $isEnabled")
        Log.d(TAG, "   - isReady: $isReady")
        Log.d(TAG, "   - tts != null: ${tts != null}")
        Log.d(TAG, "   - text.isNotEmpty(): ${text.isNotEmpty()}")

        if (isEnabled && isReady && text.isNotEmpty()) {
            val result = tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "speak_$text")
            Log.d(TAG, "📢 Comando speak inviato, risultato: $result")
        } else {
            Log.w(TAG, "⚠️ Speak saltato - verificare condizioni sopra")
        }
    }

    fun isEnabled(): Boolean {
        Log.d(TAG, "❓ Controllo se TTS è abilitato: $isEnabled")
        return isEnabled
    }

    fun release() {
        Log.d(TAG, "🔚 Rilascio risorse TTS")
        tts?.shutdown()
        tts = null
        isReady = false
        appContext = null
    }

    // Funzione di debug
    fun debugStatus() {
        Log.d(TAG, "🔍 DEBUG STATUS:")
        Log.d(TAG, "   - appContext: ${appContext != null}")
        Log.d(TAG, "   - isEnabled: $isEnabled")
        Log.d(TAG, "   - isReady: $isReady")
        Log.d(TAG, "   - tts: ${tts != null}")

        if (appContext != null) {
            val prefs = appContext!!.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val savedEnabled = prefs.getBoolean("tts_enabled", false)
            Log.d(TAG, "   - Preferenza salvata: $savedEnabled")
        }
    }
}

// Extension function per applicare TTS a qualsiasi View
fun View.enableAutoTTS() {
    Log.d("TTSHelper", "🔍 Scansione view: ${this::class.simpleName}")

    if (!TTSHelper.isEnabled()) {
        Log.d("TTSHelper", "⚠️ TTS disabilitato - skippo view")
        return
    }

    if (this is TextView && text.isNotEmpty()) {
        Log.d("TTSHelper", "📝 Trovata TextView con testo: '${text.toString().take(50)}...'")

        // Assicurati che sia cliccabile
        isClickable = true
        isFocusable = true

        setOnClickListener {
            Log.d("TTSHelper", "👆 Click su TextView: '${text.toString().take(50)}...'")
            TTSHelper.speak(text.toString())
        }

        // Aggiungi anche un test immediato per vedere se il TTS funziona
        Log.d("TTSHelper", "🧪 Test immediato TTS per questa TextView")
        post {
            TTSHelper.speak("Test: ${text.toString().take(20)}")
        }
    }

    // Salta RecyclerView - gli item vengono gestiti nell'Adapter
    if (this is RecyclerView) {
        Log.d("TTSHelper", "📱 Saltato RecyclerView - gestito nell'adapter")
        return
    }

    if (this is ViewGroup) {
        Log.d("TTSHelper", "📂 Scansione ViewGroup con ${childCount} figli")
        for (i in 0 until childCount) {
            getChildAt(i).enableAutoTTS()
        }
    }
}

// Extension function per Fragment
fun Fragment.enableTTS() {
    Log.d("TTSHelper", "🧩 Abilitazione TTS per fragment: ${this::class.simpleName}")
    view?.enableAutoTTS()

    // Debug status
    TTSHelper.debugStatus()
}

// Extension function per ViewHolder (da usare nell'Adapter)
fun RecyclerView.ViewHolder.enableTTS() {
    Log.d("TTSHelper", "📱 Abilitazione TTS per ViewHolder")
    if (TTSHelper.isEnabled()) {
        itemView.enableAutoTTS()
    }
}