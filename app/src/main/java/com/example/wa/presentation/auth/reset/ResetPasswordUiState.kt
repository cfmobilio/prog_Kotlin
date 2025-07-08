package com.example.wa.presentation.auth.reset

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)
