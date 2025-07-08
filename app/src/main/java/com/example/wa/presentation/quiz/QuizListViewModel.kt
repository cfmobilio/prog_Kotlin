package com.example.wa.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wa.R
import com.example.wa.data.model.Quiz
import com.example.wa.data.model.QuizProgress
import com.example.wa.data.repository.QuizRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class QuizListViewModel : ViewModel() {
    private val repository = QuizRepository()

    private val _quizList = MutableLiveData<List<Quiz>>()
    val quizList: LiveData<List<Quiz>> = _quizList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    init {
        initializeQuizList()
        loadAllProgress()
    }

    private fun initializeQuizList() {
        _quizList.value = listOf(
            Quiz("Privacy online", R.drawable.padlock, "privacy"),
            Quiz("Cyberbullismo", R.drawable.warning, "cyberbullismo"),
            Quiz("Phishing", R.drawable.mail, "phishing"),
            Quiz("Dipendenza dai social", R.drawable.hourglass, "dipendenza"),
            Quiz("Fake News", R.drawable.fake, "fake"),
            Quiz("Sicurezza account", R.drawable.shield, "sicurezza"),
            Quiz("Truffe online", R.drawable.scam, "truffe"),
            Quiz("Protezione dati", R.drawable.security, "dati"),
            Quiz("Netiquette", R.drawable.netiquette, "netiquette"),
            Quiz("Navigazione sicura", R.drawable.secure, "navigazione")
        )
    }

    fun loadAllProgress() {
        _isLoading.value = true

        viewModelScope.launch {
            repository.loadAllProgress()
                .onSuccess { progressList ->
                    updateQuizProgress(progressList)
                    _isLoading.value = false
                }
                .onFailure { exception ->
                    _error.value = exception.message
                    _isLoading.value = false
                }
        }
    }

    private fun updateQuizProgress(progressList: List<QuizProgress>) {
        val currentList = _quizList.value ?: return
        val updatedList = currentList.map { quiz ->
            val progress = progressList.find { it.argomento == quiz.argomentoKey }
            quiz.copy(percentuale = progress?.percentuale ?: 0)
        }
        _quizList.value = updatedList
    }

    fun refreshProgress() {
        loadAllProgress()
    }
}