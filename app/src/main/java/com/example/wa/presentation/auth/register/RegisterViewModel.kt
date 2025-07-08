package com.example.wa.presentation.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wa.data.repository.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableLiveData(RegisterUiState())
    val uiState: LiveData<RegisterUiState> = _uiState

    fun registerWithEmail(name: String, email: String, password: String, repeatPassword: String) {
        // Validazione input
        when {
            name.isBlank() || email.isBlank() || password.isBlank() || repeatPassword.isBlank() -> {
                _uiState.value = _uiState.value?.copy(
                    errorMessage = "Completa tutti i campi"
                )
                return
            }
            password != repeatPassword -> {
                _uiState.value = _uiState.value?.copy(
                    errorMessage = "Le password non coincidono"
                )
                return
            }
            password.length < 6 -> {
                _uiState.value = _uiState.value?.copy(
                    errorMessage = "La password deve contenere almeno 6 caratteri"
                )
                return
            }
        }

        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.createUserWithEmail(email, password, name)
            _uiState.value = if (result.isSuccess) {
                _uiState.value?.copy(
                    isLoading = false,
                    isRegistrationSuccessful = true,
                    shouldNavigateToHome = true
                )
            } else {
                _uiState.value?.copy(
                    isLoading = false,
                    errorMessage = "Errore: ${result.exceptionOrNull()?.message}"
                )
            }
        }
    }

    fun registerWithGoogle(idToken: String) {
        _uiState.value = _uiState.value?.copy(isLoading = true)

        viewModelScope.launch {
            val result = authRepository.signInWithGoogleAndSaveUser(idToken)
            _uiState.value = if (result.isSuccess) {
                _uiState.value?.copy(
                    isLoading = false,
                    isRegistrationSuccessful = true,
                    shouldNavigateToHome = true
                )
            } else {
                _uiState.value?.copy(
                    isLoading = false,
                    errorMessage = "Errore con Google: ${result.exceptionOrNull()?.message}"
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

class RegisterViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}