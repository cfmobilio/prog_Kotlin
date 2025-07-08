package com.example.wa.data.repository

import com.example.wa.data.model.Feedback
import kotlinx.coroutines.delay

class FeedbackRepository {

    suspend fun sendFeedback(feedback: Feedback): Result<String> {
        return try {
            // Simula invio feedback
            delay(1000)
            Result.success("Feedback inviato con successo")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendEmail(subject: String, body: String): Result<String> {
        return try {
            // Simula invio email
            delay(500)
            Result.success("Email preparata")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}