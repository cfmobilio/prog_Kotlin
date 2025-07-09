package com.example.wa.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun loginWithEmail(email: String, password: String) {
        _loginState.value = LoginState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginState.value = if (task.isSuccessful) {
                    LoginState.Success
                } else {
                    LoginState.Error(task.exception?.message ?: "Errore imprevisto")
                }
            }
    }

    fun loginWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        _loginState.value = LoginState.Loading
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                _loginState.value = if (task.isSuccessful) {
                    LoginState.Success
                } else {
                    LoginState.Error(task.exception?.message ?: "Errore autenticazione Google")
                }
            }
    }

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}


