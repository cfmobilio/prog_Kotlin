package com.example.wa.data.repository

// QuizRepository.kt
import com.example.wa.data.model.Question
import com.example.wa.data.model.QuizProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class QuizRepository {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val quizToBadgeMap = mapOf(
        "phishing" to "badge_phishing",
        "privacy" to "badge_privacy",
        "cyberbullismo" to "badge_cyberbullismo",
        "fake_news" to "badge_fake_news",
        "password" to "badge_password",
        "truffe" to "badge_truffe",
        "identita" to "badge_identita",
        "social" to "badge_social",
        "cybercrimine" to "badge_cybercrimine",
        "virus" to "badge_virus"
    )

    suspend fun loadQuestions(argomento: String): Result<List<Question>> {
        return try {
            val result = db.collection("quiz_$argomento").get().await()
            val domande = result.map { it.toObject(Question::class.java) }
            Result.success(domande)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveProgress(argomento: String, progress: QuizProgress): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            val data = mapOf(
                "domandaCorrente" to progress.domandaCorrente,
                "punteggio" to progress.punteggio,
                "percentuale" to progress.percentuale
            )

            db.collection("progressi_utente")
                .document(uid)
                .collection("argomenti")
                .document(argomento)
                .set(data, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadProgress(argomento: String): Result<QuizProgress> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            val document = db.collection("progressi_utente")
                .document(uid)
                .collection("argomenti")
                .document(argomento)
                .get()
                .await()

            if (document.exists()) {
                val progress = QuizProgress(
                    argomento = argomento,
                    domandaCorrente = document.getLong("domandaCorrente")?.toInt() ?: 0,
                    punteggio = document.getLong("punteggio")?.toInt() ?: 0,
                    percentuale = document.getLong("percentuale")?.toInt() ?: 0
                )
                Result.success(progress)
            } else {
                Result.success(QuizProgress(argomento))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun loadAllProgress(): Result<List<QuizProgress>> {
        return try {
            val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            val documents = db.collection("progressi_utente")
                .document(uid)
                .collection("argomenti")
                .get()
                .await()

            val progressList = documents.map { doc ->
                QuizProgress(
                    argomento = doc.id,
                    domandaCorrente = doc.getLong("domandaCorrente")?.toInt() ?: 0,
                    punteggio = doc.getLong("punteggio")?.toInt() ?: 0,
                    percentuale = doc.getLong("percentuale")?.toInt() ?: 0
                )
            }

            Result.success(progressList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun unlockBadge(quizId: String): Result<Unit> {
        return try {
            val badgeId = quizToBadgeMap[quizId] ?: throw Exception("Badge not found for quiz: $quizId")
            val uid = auth.currentUser?.uid ?: throw Exception("User not authenticated")

            db.collection("users")
                .document(uid)
                .update("badges.$badgeId", true)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}