package com.example.wa.presentation.auth.register

import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    fun registerWithEmail(name: String, email: String, password: String) {
        _registerState.value = RegisterState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        saveUserToFirestore(user.uid, name, email)
                    }
                } else {
                    _registerState.value = RegisterState.Error(task.exception?.message ?: "Errore imprevisto")
                }
            }
    }

    fun registerWithGoogle(account: GoogleSignInAccount) {
        _registerState.value = RegisterState.Loading
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        val name = user.displayName ?: ""
                        val email = user.email ?: ""
                        saveUserToFirestore(user.uid, name, email)
                    }
                } else {
                    _registerState.value = RegisterState.Error(task.exception?.message ?: "Errore Google")
                }
            }
    }

    private fun saveUserToFirestore(uid: String, name: String, email: String) {
        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "badges" to hashMapOf<String, Boolean>()
        )

        db.collection("users").document(uid).set(userMap)
            .addOnSuccessListener {
                val topics = listOf(
                    "cybersecurity", "privacy", "social_engineering",
                    "dataprotection", "geopolitics", "phishing",
                    "anonymity", "blocked_content", "navigation"
                )
                for (topic in topics) {
                    val progress = mapOf(
                        "percentuale" to 0,
                        "domandaCorrente" to 0,
                        "punteggio" to 0
                    )
                    db.collection("progressi_utente")
                        .document(uid)
                        .collection("argomenti")
                        .document(topic)
                        .set(progress)
                }
                _registerState.value = RegisterState.Success
            }
            .addOnFailureListener {
                _registerState.value = RegisterState.Error("Errore salvataggio Firestore")
            }
    }

    sealed class RegisterState {
        object Idle : RegisterState()
        object Loading : RegisterState()
        object Success : RegisterState()
        data class Error(val message: String) : RegisterState()
    }
}


