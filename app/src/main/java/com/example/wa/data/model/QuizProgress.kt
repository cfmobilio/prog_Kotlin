package com.example.wa.data.model

data class QuizProgress(
    val argomento: String,
    val domandaCorrente: Int = 0,
    val punteggio: Int = 0,
    val percentuale: Int = 0
)