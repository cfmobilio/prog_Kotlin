package com.example.wa.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wa.data.model.Topic
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TopicsViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _topic = MutableLiveData<Topic>()
    val topic: LiveData<Topic> = _topic

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadTopic(tipo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val document = firestore.collection("info_argomenti")
                    .document(tipo)
                    .get()
                    .await()

                if (document.exists()) {
                    val topicData = document.toObject(Topic::class.java)
                    if (topicData != null) {
                        _topic.value = topicData
                    } else {
                        _error.value = "Errore nella conversione dei dati"
                        _topic.value = Topic(
                            titolo = "Errore",
                            descrizione = "Impossibile convertire i dati da Firestore."
                        )
                    }
                } else {
                    _error.value = "Documento non trovato"
                    _topic.value = Topic(
                        titolo = "Non trovato",
                        descrizione = "L'argomento richiesto non è stato trovato."
                    )
                }
            } catch (exception: Exception) {
                _error.value = exception.message ?: "Errore nel caricamento"
                _topic.value = Topic(
                    titolo = "Errore",
                    descrizione = "Impossibile caricare i dati da Firestore: ${exception.message}"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
}