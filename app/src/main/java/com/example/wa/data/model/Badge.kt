package com.example.wa.data.model

data class Badge(
    val badgeid: String,
    val isUnlocked: Boolean = false,
    val quizId: String
)