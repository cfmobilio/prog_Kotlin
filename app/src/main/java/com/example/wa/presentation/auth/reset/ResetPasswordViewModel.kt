package com.example.wa.presentation.auth.reset

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.repository.AuthRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableLiveData<ResetPasswordUiState>()
    val uiState: LiveData<ResetPasswordUiState> = _uiState

    fun sendPasswordResetEmail(email: String) {
        if (email.isBlank()) {
            _uiState.value = ResetPasswordUiState(
                errorMessage = "Inserisci un'email valida"
            )
            return
        }

        _uiState.value = ResetPasswordUiState(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.sendPasswordResetEmail(email)
            _uiState.value = if (result.isSuccess) {
                ResetPasswordUiState(
                    isLoading = false,
                    successMessage = "Email inviata! Controlla la tua casella."
                )
            } else {
                ResetPasswordUiState(
                    isLoading = false,
                    errorMessage = "Errore: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = ResetPasswordUiState()
    }
}

