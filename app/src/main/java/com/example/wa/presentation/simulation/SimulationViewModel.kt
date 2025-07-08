package com.example.wa.presentation.simulation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wa.data.model.Argomenti
import com.example.wa.data.repository.SimulationRepository


class SimulationViewModel : ViewModel(){
    private val repository=SimulationRepository()

    private val _simulation = MutableLiveData<List<Argomenti>>()
    val sim: LiveData<List<Argomenti>> = _simulation

    private val _simulationMap = MutableLiveData<Map<String, String>>()
    val simulationMap: LiveData<Map<String, String>> = _simulationMap

    init {
        loadSimulazioni()
    }

    private fun loadSimulazioni() {
        _simulation.value=repository.getArg()
        _simulationMap.value=repository.getArgomentiMap()

    }


}

