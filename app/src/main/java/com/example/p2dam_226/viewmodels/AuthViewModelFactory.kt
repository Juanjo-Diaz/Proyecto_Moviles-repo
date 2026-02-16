package com.example.p2dam_226.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p2dam_226.firebase.ServiceLocator

class AuthViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(ServiceLocator.authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
