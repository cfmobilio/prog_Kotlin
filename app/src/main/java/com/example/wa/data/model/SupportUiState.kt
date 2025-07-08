package com.example.wa.data.model

data class SupportUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val shouldClearForm: Boolean = false,
    val shouldOpenEmail: Boolean = false
)