package com.example.wa.data.repository

import com.example.wa.data.model.Insight
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.FirebaseFirestore


class InsightRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getInisght(): Map<String, Insight> {
        return try {
            val snapshot = firestore.collection("approfondimenti")
                .get()
                .await()

            val insightMap = mutableMapOf<String, Insight>()

            for (document in snapshot.documents) {
                val titolo = document.getString("titolo") ?: ""
                val descrizione = document.getString("descrizione") ?: ""

                // Crea l'oggetto App con i dati recuperati
                val insight = Insight(
                    titolo = titolo,
                    descrizione = descrizione,

                )

                insightMap[document.id] = insight
            }

            insightMap
        } catch (e: Exception) {
            throw e
        }
    }
}