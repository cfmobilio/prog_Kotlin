package com.example.wa.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.wa.data.model.Topic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class TopicRepository {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getTopicByType(tipo: String): Result<Topic> {
        return try {
            val document = db.collection("argomenti").document(tipo).get().await()
            if (document.exists()) {
                val topic = Topic(
                    titolo = document.getString("titolo") ?: "",
                    descrizione = document.getString("descrizione") ?: "",
                    videoUrl = document.getString("videoUrl") ?: ""
                )
                Result.success(topic)
            } else {
                Result.failure(Exception("Documento non trovato"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}