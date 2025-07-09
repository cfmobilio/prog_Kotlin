package com.example.wa.presentation.auth.reset

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ResetPasswordViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _resetState = MutableStateFlow<ResetState>(ResetState.Idle)
    val resetState: StateFlow<ResetState> = _resetState

    fun sendResetEmail(email: String) {
        _resetState.value = ResetState.Loading
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                _resetState.value = if (task.isSuccessful) {
                    ResetState.Success
                } else {
                    ResetState.Error(task.exception?.message ?: "Errore reset")
                }
            }
    }

    sealed class ResetState {
        object Idle : ResetState()
        object Loading : ResetState()
        object Success : ResetState()
        data class Error(val message: String) : ResetState()
    }
}

