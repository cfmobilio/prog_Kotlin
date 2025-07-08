package com.example.wa.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wa.data.model.Subject
import com.example.wa.data.repository.SubjectRepository

class HomeViewModel : ViewModel() {
    private val repository = SubjectRepository()

    private val _subjects = MutableLiveData<List<Subject>>()
    val subjects: LiveData<List<Subject>> = _subjects

    private val _argomentiMap = MutableLiveData<Map<String, String>>()
    val argomentiMap: LiveData<Map<String, String>> = _argomentiMap

    init {
        loadSubjects()
    }

    private fun loadSubjects() {
        _subjects.value = repository.getSubjects()
        _argomentiMap.value = repository.getArgomentiMap()
    }
}