package com.example.wa.data.model

data class AccessibilityUiState(
    val highContrastEnabled: Boolean = false,
    val isTtsInitialized: Boolean = false,
    val isTtsInitializing: Boolean = false,
    val isSpeaking: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)