package com.example.wa.data.model

data class Question(
    val testo: String = "",
    val opzioni: List<String> = listOf(),
    val rispostaCorretta: Int = 0
)