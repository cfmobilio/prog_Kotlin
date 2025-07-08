package com.example.wa.data.model

data class SettingsUiState(
    val accessibilityMode: Boolean = false,
    val notifications: Boolean = true,
    val theme: String = "light",
    val isLoading: Boolean = true,
    val needsRestart: Boolean = false,
    val errorMessage: String? = null
)