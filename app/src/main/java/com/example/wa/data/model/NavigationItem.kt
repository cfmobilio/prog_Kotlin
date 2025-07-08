package com.example.wa.data.model

sealed class NavigationDestination {
    object Home : NavigationDestination()
    object Profile : NavigationDestination()
    object Quiz : NavigationDestination()
    object Simulation : NavigationDestination()
    object Insight : NavigationDestination()
    object Emergency : NavigationDestination()

}
