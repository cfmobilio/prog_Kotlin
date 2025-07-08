package com.example.wa.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wa.presentation.quiz.QuizQuestionViewModel

class QuizQuestionViewModelFactory(private val argomento: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizQuestionViewModel(argomento) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}