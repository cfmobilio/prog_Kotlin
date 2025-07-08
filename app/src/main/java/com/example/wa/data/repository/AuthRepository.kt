package com.example.wa.data.repository

import android.app.Activity
import com.example.wa.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    suspend fun signInWithEmail(email: String, password: String): Result<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if (result.user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Login fallito"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogle(idToken: String): Result<Unit> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            if (result.user != null) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Login con Google fallito"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUser() = auth.currentUser

    fun getGoogleSignInClient(activity: Activity): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(activity, gso)
    }

    suspend fun createUserWithEmail(email: String, password: String, name: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("User ID non disponibile")

            // Salva i dati dell'utente su Firestore
            saveUserToFirestore(userId, name, email)

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signInWithGoogleAndSaveUser(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()

            val firebaseUser = result.user ?: throw Exception("User non disponibile")
            val name = firebaseUser.displayName ?: ""
            val email = firebaseUser.email ?: ""

            // Salva i dati dell'utente su Firestore
            saveUserToFirestore(firebaseUser.uid, name, email)

            Result.success(firebaseUser.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun saveUserToFirestore(uid: String, name: String, email: String) {
        val userMap = hashMapOf(
            "name" to name,
            "email" to email,
            "badges" to hashMapOf<String, Boolean>()
        )

        db.collection("users").document(uid).set(userMap)

        // Inizializza i progressi quiz per ogni argomento
        val argomenti = listOf(
            "cybersecurity", "privacy", "social_engineering",
            "dataprotection", "geopolitics", "phishing",
            "anonymity", "blocked_content", "navigation"
        )

        argomenti.forEach { argomento ->
            val progresso = mapOf(
                "percentuale" to 0,
                "domandaCorrente" to 0,
                "punteggio" to 0
            )
            db.collection("progressi_utente")
                .document(uid)
                .collection("argomenti")
                .document(argomento)
                .set(progresso)
        }
    }
}