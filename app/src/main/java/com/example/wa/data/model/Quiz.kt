package com.example.wa.data.model

data class Quiz(
    val titolo: String,
    val icona: Int,
    val argomentoKey: String,
    var percentuale: Int = 0
)