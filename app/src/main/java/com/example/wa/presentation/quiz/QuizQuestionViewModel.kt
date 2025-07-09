package com.example.wa.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.Question
import com.example.wa.data.model.QuizProgress
import com.example.wa.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizQuestionViewModel(private val argomento: String) : ViewModel() {
    private val repository = QuizRepository()

    private val _domande = MutableLiveData<List<Question>>()
    val domande: LiveData<List<Question>> = _domande

    private val _currentQuestionIndex = MutableLiveData<Int>()
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _quizCompleted = MutableLiveData<Boolean>()
    val quizCompleted: LiveData<Boolean> = _quizCompleted

    private val _finalScore = MutableLiveData<Int>()
    val finalScore: LiveData<Int> = _finalScore

    init {
        loadProgress()
    }

    private fun loadProgress() {
        viewModelScope.launch {
            repository.loadProgress(argomento)
                .onSuccess { progress ->
                    _currentQuestionIndex.value = progress.domandaCorrente
                    _score.value = progress.punteggio
                    loadQuestions()
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    // Fallback: start from beginning
                    _currentQuestionIndex.value = 0
                    _score.value = 0
                    loadQuestions()
                }
        }
    }

    private fun loadQuestions() {
        _isLoading.value = true

        viewModelScope.launch {
            repository.loadQuestions(argomento)
                .onSuccess { questions ->
                    _domande.value = questions
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
        }
    }

    fun submitAnswer(selectedAnswer: Int) {
        val questions = _domande.value ?: return
        val currentIndex = _currentQuestionIndex.value ?: return
        val currentScore = _score.value ?: return

        if (currentIndex < questions.size) {
            val isCorrect = selectedAnswer == questions[currentIndex].rispostaCorretta
            val newScore = if (isCorrect) currentScore + 1 else currentScore

            _score.value = newScore

            val nextIndex = currentIndex + 1
            if (nextIndex < questions.size) {
                _currentQuestionIndex.value = nextIndex  // <-- qui incrementi domandaCorrente
                saveProgress()
            } else {
                completeQuiz(newScore)
            }
        }
    }

    fun goToPreviousQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: return
        if (currentIndex > 0) {
            _currentQuestionIndex.value = currentIndex - 1
        }
    }

    private fun completeQuiz(finalScore: Int) {
        val questions = _domande.value ?: return
        val percentage = (finalScore.toFloat() / questions.size * 100).toInt()

        _finalScore.value = finalScore

        viewModelScope.launch {
            val progress = QuizProgress(
                argomento = argomento,
                domandaCorrente = 0,
                punteggio = 0,
                percentuale = percentage
            )

            repository.saveProgress(argomento, progress)
                .onSuccess {
                    _quizCompleted.value = true
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    _quizCompleted.value = true
                }
        }
    }

    private fun saveProgress() {
        viewModelScope.launch {
            val progress = QuizProgress(
                argomento = argomento,
                domandaCorrente = _currentQuestionIndex.value ?: 0,
                punteggio = _score.value ?: 0
            )
            println("Saving progress: domandaCorrente=${progress.domandaCorrente}, punteggio=${progress.punteggio}")
            repository.saveProgress(argomento, progress)
        }
    }


    fun saveCurrentProgress() {
        saveProgress()
    }

    fun getFinalPercentage(): Int {
        val questions = _domande.value ?: return 0
        val score = _finalScore.value ?: return 0
        return (score.toFloat() / questions.size * 100).toInt()
    }
}