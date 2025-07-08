package com.example.wa.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.wa.data.model.Question
import com.example.wa.data.model.QuizResultLevel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InQuizRepository {
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loadQuizQuestions(): Result<List<Question>> {
        return try {
            val result = db.collection("quiz_intro").get().await()
            val questions = result.documents.mapNotNull { document ->
                try {
                    document.toObject(Question::class.java)
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(questions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun determineQuizLevel(score: Int): QuizResultLevel {
        return when (score) {
            in 0..3 -> QuizResultLevel.BASE
            in 4..6 -> QuizResultLevel.INTERMEDIATE
            else -> QuizResultLevel.ADVANCED
        }
    }
}