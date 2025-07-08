package com.example.wa.presentation.emergency

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wa.data.repository.EmergencyRepository
import com.example.wa.data.model.Emergency

class EmergencyViewModel : ViewModel() {
    private val repository = EmergencyRepository()

    private val _emergencies = MutableLiveData<List<Emergency>>()
    val emergencies: LiveData<List<Emergency>> = _emergencies

    init {
        loadEmergencies()
    }

    private fun loadEmergencies() {
        _emergencies.value = repository.getEmergencies()
    }
}