package com.example.wa.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.NavigationDestination
import com.example.wa.data.model.NavigationEvent
import com.example.wa.data.model.ProfileData
import com.example.wa.data.model.ProgressData
import com.example.wa.data.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    private val _profileData = MutableLiveData<ProfileData>()
    val profileData: LiveData<ProfileData> = _profileData

    private val _badges = MutableLiveData<Map<String, Boolean>>()
    val badges: LiveData<Map<String, Boolean>> = _badges

    private val _progressData = MutableLiveData<ProgressData>()
    val progressData: LiveData<ProgressData> = _progressData

    private val _uiState = MutableLiveData<ProfileUiState>()
    val uiState: LiveData<ProfileUiState> = _uiState

    // Cambiato a nullable per poter gestire lo stato "nessun evento"
    private val _navigationEvent = MutableLiveData<NavigationEvent?>()
    val navigationEvent: LiveData<NavigationEvent?> = _navigationEvent

    // Cambiato a nullable per poter gestire lo stato "nessun messaggio"
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    init {
        loadUserData()
    }

    fun loadUserData() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading

            try {
                val result = repository.getCurrentUserProfile()
                result.fold(
                    onSuccess = { profile ->
                        _profileData.value = profile
                        _badges.value = profile.badges
                        updateProgressData(profile.badges)
                        _uiState.value = ProfileUiState.Success
                    },
                    onFailure = { error ->
                        _uiState.value = ProfileUiState.Error(error.message ?: "Errore sconosciuto")
                    }
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Errore durante il caricamento: ${e.message}")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                repository.logout()
                _navigationEvent.value = NavigationEvent.NavigateToDestination(NavigationDestination.Profile) // Navigherà al login dal fragment
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Errore durante il logout: ${e.message}")
            }
        }
    }

    fun onBadgeClicked(badgeKey: String) {
        val descriptions = mapOf(
            "lock" to "Badge: Privacy Online",
            "compass" to "Badge Navigazione",
            "target" to "Badge Obiettivi",
            "eyes" to "Badge Osservatore",
            "banned" to "Badge Anti-Phishing",
            "floppy_disk" to "Badge Backup",
            "private_detective" to "Badge Investigatore",
            "key" to "Badge: Sicurezza informatica",
            "earth" to "Badge Globale"
        )

        _toastMessage.value = descriptions[badgeKey] ?: "Informazioni non disponibili"
    }

    fun onNavigationClicked(destination: NavigationDestination) {
        _navigationEvent.value = NavigationEvent.NavigateToDestination(destination)
    }

    private fun updateProgressData(badges: Map<String, Boolean>) {
        val unlocked = badges.count { it.value }
        val total = badges.size
        val percent = if (total > 0) (unlocked.toFloat() / total * 100).toInt() else 0

        _progressData.value = ProgressData(
            unlocked = unlocked,
            total = total,
            percentage = percent
        )
    }

    // Funzione per resettare gli eventi di navigazione dopo che sono stati gestiti
    fun onNavigationEventHandled() {
        _navigationEvent.value = null
    }

    // Funzione per resettare i toast dopo che sono stati gestiti
    fun onToastMessageHandled() {
        _toastMessage.value = null
    }
}