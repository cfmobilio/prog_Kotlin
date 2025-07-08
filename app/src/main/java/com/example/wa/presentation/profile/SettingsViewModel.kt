package com.example.wa.presentation.profile

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences =
        application.getSharedPreferences("AppPrefs", Application.MODE_PRIVATE)

    private val _accessibilityEnabled = MutableStateFlow(prefs.getBoolean("accessibility_mode", false))
    val accessibilityEnabled: StateFlow<Boolean> = _accessibilityEnabled

    fun toggleAccessibility(enabled: Boolean) {
        prefs.edit().putBoolean("accessibility_mode", enabled).apply()
        _accessibilityEnabled.value = enabled
    }
}
