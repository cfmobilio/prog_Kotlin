package com.example.wa.presentation.simulation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.NavigationDestination
import com.example.wa.data.model.NavigationEvent
import com.example.wa.data.model.Simulazione
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class SimulationUiState(
    val isLoading: Boolean = false,
    val simulazione: Simulazione? = null,
    val error: String? = null,
    val feedback: String? = null,
    val isAnswerCorrect: Boolean? = null
)

class SituationViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(SimulationUiState())
    val uiState: StateFlow<SimulationUiState> = _uiState

    private val _navigationEvent = MutableStateFlow<NavigationEvent?>(null)
    val navigationEvent: StateFlow<NavigationEvent?> = _navigationEvent

    fun onNavigationClicked(destination: NavigationDestination) {
        _navigationEvent.value = NavigationEvent.NavigateToDestination(destination)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun loadSimulazione(tipo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val document = firestore.collection("simulazioni")
                    .document(tipo)
                    .get()
                    .await()

                if (document.exists()) {
                    Log.d("SituationViewModel", "Dati Firestore: ${document.data}")

                    val situationData = document.toObject(Simulazione::class.java)

                    if (situationData != null) {
                        _uiState.update {
                            it.copy(simulazione = situationData, error = null)
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                error = "Errore nella conversione dei dati",
                                simulazione = Simulazione(
                                    titolo = "Errore",
                                    descrizione = "Impossibile convertire i dati da Firestore."
                                )
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            error = "Documento non trovato",
                            simulazione = Simulazione(
                                titolo = "Non trovato",
                                descrizione = "L'argomento richiesto non è stato trovato."
                            )
                        )
                    }
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        error = exception.message ?: "Errore nel caricamento",
                        simulazione = Simulazione(
                            titolo = "Errore",
                            descrizione = "Impossibile caricare i dati da Firestore: ${exception.message}"
                        )
                    )
                }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun checkAnswer(selectedAnswer: Int) {
        val simulazione = _uiState.value.simulazione ?: return

        val isCorrect = selectedAnswer == simulazione.rispostaCorretta
        val feedback = if (isCorrect) {
            simulazione.feedbackPositivo
        } else {
            simulazione.feedbackNegativo
        }

        _uiState.update {
            it.copy(
                feedback = feedback,
                isAnswerCorrect = isCorrect
            )
        }
    }

    fun onAnswerSelected(isCorrect: Boolean) {
        val feedback = if (isCorrect) {
            "Risposta corretta!"
        } else {
            "Risposta sbagliata. Riprova!"
        }

        _uiState.value = _uiState.value.copy(feedback = feedback)
    }

    fun clearFeedback() {
        _uiState.update {
            it.copy(feedback = null, isAnswerCorrect = null)
        }
    }
}
