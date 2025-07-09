package com.example.wa.presentation.auth.login

data class LoginUiState(
    val errorMessage: String? = null,
    val shouldNavigateToHome: Boolean = false
)