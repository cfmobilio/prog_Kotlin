package com.example.wa.data.model

sealed class NavigationEvent {
    data class NavigateToInsight(val argomento: String) : NavigationEvent()
    data class NavigateToDestination(val destination: NavigationDestination) : NavigationEvent()
}