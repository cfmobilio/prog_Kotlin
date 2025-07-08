package com.example.wa.presentation.insight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wa.data.model.App
import com.example.wa.data.repository.ExtraRepository

class ExtraViewModel : ViewModel() {
    private val repository = ExtraRepository()

    private val _apps = MutableLiveData<List<App>>()
    val apps: LiveData<List<App>> = _apps

    private val _argomentiMap = MutableLiveData<Map<String, String>>()
    val argomentiMap: LiveData<Map<String, String>> = _argomentiMap

    init {
        loadApps()
    }

    private fun loadApps() {
            _apps.value = repository.getApp()
            _argomentiMap.value = repository.getArgomentiMap()

    }

}