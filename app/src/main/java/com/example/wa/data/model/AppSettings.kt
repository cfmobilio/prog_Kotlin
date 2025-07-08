package com.example.wa.data.model

data class AppSettings(
    val accessibilityMode: Boolean = false,
    val notifications: Boolean = true,
    val theme: String = "light"
)