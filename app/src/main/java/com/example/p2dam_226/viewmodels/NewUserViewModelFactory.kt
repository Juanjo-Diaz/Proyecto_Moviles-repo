package com.example.p2dam_226.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.p2dam_226.firebase.ServiceLocator

class NewUserViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewUserViewModel::class.java)) {
            return NewUserViewModel(
                ServiceLocator.authRepository,
                ServiceLocator.userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
