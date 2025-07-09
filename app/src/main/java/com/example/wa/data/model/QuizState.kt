package com.example.wa.data.model

data class QuizState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val selectedAnswer: Int = -1,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val currentQuestion: Question?
        get() = if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else null

    val isFirstQuestion: Boolean
        get() = currentQuestionIndex == 0

    val isLastQuestion: Boolean
        get() = currentQuestionIndex == questions.size - 1

    val totalQuestions: Int
        get() = questions.size

}