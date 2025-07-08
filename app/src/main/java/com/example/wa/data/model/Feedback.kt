package com.example.wa.data.model

data class Feedback(
    val rating: Int,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)