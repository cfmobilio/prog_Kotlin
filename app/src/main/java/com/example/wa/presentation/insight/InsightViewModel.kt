package com.example.wa.presentation.insight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.Insight
import com.example.wa.data.model.NavigationDestination
import com.example.wa.data.model.NavigationEvent
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class InsightViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _insight = MutableLiveData<Insight?>()
    val insight: LiveData<Insight?> = _insight

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _navigationEvent = MutableLiveData<NavigationEvent?>()
    val navigationEvent: LiveData<NavigationEvent?> = _navigationEvent

    fun onNavigationClicked(destination: NavigationDestination) {
        _navigationEvent.value = NavigationEvent.NavigateToDestination(destination)
    }

    fun clearNavigationEvent() {
        _navigationEvent.value = null
    }

    fun loadInsight(tipo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val document = firestore.collection("approfondimenti")
                    .document(tipo)
                    .get()
                    .await()

                if (document.exists()) {

                    val insightData = document.toObject(Insight::class.java)

                    if (insightData != null) {
                        _insight.value = insightData
                    } else {
                        _error.value = "Errore nella conversione dei dati"
                        _insight.value = Insight(
                            titolo = "Errore",
                            descrizione = "Impossibile convertire i dati da Firestore."
                        )
                    }
                } else {
                    _error.value = "Documento non trovato"
                    _insight.value = Insight(
                        titolo = "Non trovato",
                        descrizione = "L'argomento richiesto non è stato trovato."
                    )
                }
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Errore nel caricamento"
                _insight.value = Insight(
                    titolo = "Errore",
                    descrizione = "Impossibile caricare i dati da Firestore: ${exception.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}
