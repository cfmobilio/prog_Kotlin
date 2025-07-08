package com.example.wa.data.model

sealed class NavigationDestination {
    object Home : NavigationDestination()
    object Profile : NavigationDestination()
    object Quiz : NavigationDestination()
    object Simulation : NavigationDestination()
    object Insight : NavigationDestination()
    object Emergency : NavigationDestination()
    object Modulo : NavigationDestination()
    data class SimulationSituation(val argomento: String) : NavigationDestination()
    data class ModuloQuestion(val argomentoKey: String) : NavigationDestination()


}
