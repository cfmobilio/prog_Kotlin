package com.example.wa.presentation.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableLiveData(LoginUiState())
    val uiState: LiveData<LoginUiState> = _uiState

    fun signInWithEmail(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value?.copy(
                errorMessage = "Inserisci tutti i campi"
            )
            return
        }

        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)
            _uiState.value = if (result.isSuccess) {
                _uiState.value?.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    shouldNavigateToHome = true
                )
            } else {
                _uiState.value?.copy(
                    isLoading = false,
                    errorMessage = "Credenziali errate o errore di rete"
                )
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            _uiState.value = if (result.isSuccess) {
                _uiState.value?.copy(
                    isLoading = false,
                    isLoginSuccessful = true,
                    shouldNavigateToHome = true
                )
            } else {
                _uiState.value?.copy(
                    isLoading = false,
                    errorMessage = "Autenticazione Google fallita"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value?.copy(errorMessage = null)
    }

    fun navigationCompleted() {
        _uiState.value = _uiState.value?.copy(shouldNavigateToHome = false)
    }
}