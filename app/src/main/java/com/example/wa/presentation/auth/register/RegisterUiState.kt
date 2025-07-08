package com.example.wa.presentation.auth.register

data class RegisterUiState(
    val isLoading: Boolean = false,
    val isRegistrationSuccessful: Boolean = false,
    val errorMessage: String? = null,
    val shouldNavigateToHome: Boolean = false
)