package com.example.wa.data.model

enum class QuizResultLevel {
    BASE, INTERMEDIATE, ADVANCED
}

data class QuizResult(
    val score: Int,
    val totalQuestions: Int,
    val level: QuizResultLevel
)