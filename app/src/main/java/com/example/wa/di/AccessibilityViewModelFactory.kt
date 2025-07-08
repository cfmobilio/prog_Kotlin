package com.example.wa.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wa.data.repository.SettingsRepository
import com.example.wa.presentation.profile.AccessibilityViewModel

class AccessibilityViewModelFactory(
    private val application: Application,
    private val settingsRepository: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccessibilityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AccessibilityViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
