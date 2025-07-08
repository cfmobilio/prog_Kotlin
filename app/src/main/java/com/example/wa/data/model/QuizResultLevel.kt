package com.example.wa.data.model

enum class QuizResultLevel {
    BASE, INTERMEDIATE, ADVANCED
}

data class QuizResult(
    val score: Int,
    val totalQuestions: Int,
    val level: QuizResultLevel
) {
    val percentage: Float
        get() = if (totalQuestions > 0) (score.toFloat() / totalQuestions) * 100f else 0f
}