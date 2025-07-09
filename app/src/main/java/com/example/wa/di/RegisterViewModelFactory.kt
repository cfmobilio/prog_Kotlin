//package com.example.wa.di
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.wa.data.repository.AuthRepository
//import com.example.wa.presentation.auth.register.RegisterViewModel
//
//class RegisterViewModelFactory(private val authRepository: AuthRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
//            @Suppress("UNCHECKED_CAST")
//            return RegisterViewModel(authRepository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}