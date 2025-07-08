package com.example.wa

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val isHighContrast = prefs.getBoolean("accessibility_mode", false)

        if (isHighContrast) {
            setTheme(R.style.HighContrastTheme)
        } else {
            setTheme(R.style.Theme_MyApp)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun applyHighContrast(enabled: Boolean) {
        val prefs = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("accessibility_mode", enabled).apply()

        if (enabled) {
            setTheme(R.style.HighContrastTheme)
        } else {
            setTheme(R.style.Theme_MyApp)
        }
        recreate()
    }
}
