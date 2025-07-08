package com.example.wa.presentation.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.repository.QuizRepository
import kotlinx.coroutines.launch

class BadgeViewModel : ViewModel() {
    private val repository = QuizRepository()

    private val _badgeUnlocked = MutableLiveData<Boolean>()
    val badgeUnlocked: LiveData<Boolean> = _badgeUnlocked

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun unlockBadge(quizId: String) {
        viewModelScope.launch {
            repository.unlockBadge(quizId)
                .onSuccess {
                    _badgeUnlocked.value = true
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
        }
    }
}