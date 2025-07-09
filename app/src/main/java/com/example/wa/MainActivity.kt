package com.example.wa

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val PREFS_NAME = "AppPrefs"
        const val KEY_HIGH_CONTRAST = "accessibility_mode"
        const val KEY_LARGE_TEXT = "large_text"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Carica le preferenze
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val highContrast = prefs.getBoolean(KEY_HIGH_CONTRAST, false)
        val largeText = prefs.getBoolean(KEY_LARGE_TEXT, false)

        // Scegli il tema da applicare in base alle preferenze
        val themeToApply = when {
            highContrast -> R.style.Theme_WA_HighContrast
            largeText -> R.style.Theme_WA_LargeText
            else -> R.style.Theme_WA
        }

        setTheme(themeToApply)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // AGGIUNTO: metodo chiamato dal Fragment
    fun applyHighContrast(enabled: Boolean) {
        updateHighContrast(enabled)
    }

    fun updateHighContrast(enabled: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_HIGH_CONTRAST, enabled).apply()
        recreate()
    }

    fun updateLargeText(enabled: Boolean) {
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_LARGE_TEXT, enabled).apply()
        recreate()
    }
}