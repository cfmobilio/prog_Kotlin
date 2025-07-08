package com.example.wa.data.model

data class Simulazione(
    val id: String = "",
    val titolo: String = "",
    val descrizione: String = "",
    val scelta: List<String> = listOf(),
    val rispostaCorretta: Int = 0,
    val feedbackPositivo: String = "",
    val feedbackNegativo: String = ""
)