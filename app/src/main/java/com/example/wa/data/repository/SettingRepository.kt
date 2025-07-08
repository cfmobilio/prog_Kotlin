package com.example.wa.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.wa.data.model.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings

    private fun loadSettings(): AppSettings {
        return AppSettings(
            accessibilityMode = prefs.getBoolean("accessibility_mode", false),
            notifications = prefs.getBoolean("notifications", true),
            theme = prefs.getString("theme", "light") ?: "light"
        )
    }

    suspend fun updateAccessibilityMode(enabled: Boolean) {
        prefs.edit().putBoolean("accessibility_mode", enabled).apply()
        _settings.value = _settings.value.copy(accessibilityMode = enabled)
    }

    suspend fun updateNotifications(enabled: Boolean) {
        prefs.edit().putBoolean("notifications", enabled).apply()
        _settings.value = _settings.value.copy(notifications = enabled)
    }

    fun getAccessibilityMode(): Boolean = prefs.getBoolean("accessibility_mode", false)
}


