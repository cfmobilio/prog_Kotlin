package com.example.wa.presentation.initialQuiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.QuizResult
import com.example.wa.data.model.QuizResultLevel
import com.example.wa.data.model.QuizState
import com.example.wa.data.repository.InQuizRepository
import kotlinx.coroutines.launch

class QuizViewModel : ViewModel() {
    private val repository = InQuizRepository()

    private val _quizState = MutableLiveData<QuizState>()
    val quizState: LiveData<QuizState> = _quizState

    private val _quizResult = MutableLiveData<QuizResult?>()
    val quizResult: LiveData<QuizResult> = _quizResult as LiveData<QuizResult>

    private val _navigationEvent = MutableLiveData<QuizResultLevel?>()
    val navigationEvent: LiveData<QuizResultLevel> = _navigationEvent as LiveData<QuizResultLevel>

    init {
        _quizState.value = QuizState()
    }

    fun loadQuestions() {
        viewModelScope.launch {
            _quizState.value = _quizState.value?.copy(isLoading = true, error = null)

            repository.loadQuizQuestions()
                .onSuccess { questions ->
                    _quizState.value = _quizState.value?.copy(
                        questions = questions,
                        isLoading = false,
                        currentQuestionIndex = 0
                    )
                }
                .onFailure { exception ->
                    _quizState.value = _quizState.value?.copy(
                        isLoading = false,
                        error = exception.message ?: "Errore nel caricamento delle domande"
                    )
                }
        }
    }

    fun selectAnswer(answerIndex: Int) {
        _quizState.value = _quizState.value?.copy(selectedAnswer = answerIndex)
    }

    fun nextQuestion() {
        val currentState = _quizState.value ?: return

        if (currentState.selectedAnswer == -1) return

        val isCorrect = currentState.selectedAnswer == currentState.currentQuestion?.rispostaCorretta
        val newScore = if (isCorrect) currentState.score + 1 else currentState.score
        val newIndex = currentState.currentQuestionIndex + 1

        if (newIndex >= currentState.questions.size) {

            val level = repository.determineQuizLevel(newScore)
            val result = QuizResult(newScore, currentState.questions.size, level)
            _quizResult.value = result
            _navigationEvent.value = level

            _quizState.value = currentState.copy(
                score = newScore,
                currentQuestionIndex = newIndex,
                selectedAnswer = -1
            )
        } else {
            _quizState.value = currentState.copy(
                score = newScore,
                currentQuestionIndex = newIndex,
                selectedAnswer = -1
            )
        }
    }

    fun previousQuestion() {
        val currentState = _quizState.value ?: return

        if (currentState.currentQuestionIndex > 0) {
            _quizState.value = currentState.copy(
                currentQuestionIndex = currentState.currentQuestionIndex - 1,
                selectedAnswer = -1
            )
        }
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }
}