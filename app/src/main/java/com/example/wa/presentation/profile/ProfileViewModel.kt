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

    private val _navigationEvent = MutableLiveData<NavigationEvent?>()
    val navigationEvent: LiveData<NavigationEvent?> = _navigationEvent

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    private val allBadgeKeys = listOf(
        "lock", "banned", "target", "eyes", "fact_check",
        "key", "private_detective", "floppy_disk", "earth", "compass"
    )

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
                _navigationEvent.value = NavigationEvent.NavigateToDestination(NavigationDestination.Profile)
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Errore durante il logout: ${e.message}")
            }
        }
    }

    fun onBadgeClicked(badgeKey: String) {
        val descriptions = mapOf(
            "lock" to "Badge: Privacy Online",
            "banned" to "Badge Cyberbullismo",
            "target" to "Badge Phishing",
            "eyes" to "Badge Dipendenza dai social",
            "fact_check" to "Badge Fake news",
            "key" to "Badge: Sicurezza informatica",
            "private_detective" to "Badge Truffe online",
            "floppy_disk" to "Badge Protezione dei dati",
            "earth" to "Badge Netiquette",
            "compass" to "Badge Navigazione sicura"
        )

        _toastMessage.value = descriptions[badgeKey] ?: "Informazioni non disponibili"
    }

    fun onNavigationClicked(destination: NavigationDestination) {
        _navigationEvent.value = NavigationEvent.NavigateToDestination(destination)
    }

    private fun updateProgressData(userBadges: Map<String, Boolean>) {
        val fullBadgeMap = allBadgeKeys.associateWith { key ->
            userBadges[key] ?: false
        }

        val unlocked = fullBadgeMap.count { it.value }
        val total = fullBadgeMap.size
        val percent = if (total > 0) (unlocked.toFloat() / total * 100).toInt() else 0

        _progressData.value = ProgressData(
            unlocked = unlocked,
            total = total,
            percentage = percent
        )

        _badges.value = fullBadgeMap
    }

    fun onNavigationEventHandled() {
        _navigationEvent.value = null
    }

    fun onToastMessageHandled() {
        _toastMessage.value = null
    }
}
