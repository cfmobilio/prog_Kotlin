package com.example.wa.presentation.auth.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val shouldNavigateToHome: Boolean = false
)