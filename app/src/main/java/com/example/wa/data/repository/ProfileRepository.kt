package com.example.wa.data.repository

import com.example.wa.data.model.ProfileData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun getCurrentUserProfile(): Result<ProfileData> {
        return try {
            val uid = auth.currentUser?.uid
                ?: return Result.failure(Exception("Utente non autenticato"))

            val document = db.collection("users").document(uid).get().await()

            if (document.exists()) {
                val name = document.getString("name") ?: "Utente"
                val badges = document.get("badges") as? Map<String, Boolean> ?: emptyMap()

                Result.success(ProfileData(name = name, badges = badges))
            } else {
                Result.failure(Exception("Profilo non trovato"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        auth.signOut()
    }

    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}