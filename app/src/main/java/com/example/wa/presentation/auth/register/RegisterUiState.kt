package com.example.wa.presentation.auth.register

data class RegisterUiState(
    val errorMessage: String? = null,
    val shouldNavigateToHome: Boolean = false
)