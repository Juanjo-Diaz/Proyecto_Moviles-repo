package com.example.p2dam_226.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p2dam_226.firebase.AuthRepository
import com.example.p2dam_226.firebase.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewUserViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _newUserUiState = MutableStateFlow<NewUserUiState>(NewUserUiState.Idle)
    val newUserUiState: StateFlow<NewUserUiState> = _newUserUiState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow() // This is email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    private val _birthdate = MutableStateFlow("")
    val birthdate: StateFlow<String> = _birthdate.asStateFlow()

    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()

    fun onUsernameChanged(value: String) {
        _username.value = value
        validate()
    }

    fun onPasswordChanged(value: String) {
        _password.value = value
        validate()
    }

    fun onConfirmPasswordChanged(value: String) {
        _confirmPassword.value = value
        validate()
    }

    fun onBirthdateChanged(value: String) {
        _birthdate.value = value
    }

    private fun validate() {
        val u = _username.value
        val p = _password.value
        val c = _confirmPassword.value
        // Validation: email not empty (could utilize Patterns.EMAIL_ADDRESS but simple check for now), 
        // password >= 6, confirm matches password.
        _isFormValid.value = u.isNotEmpty() && p.length >= 6 && p == c
    }

    fun register() {
        val email = _username.value
        val pass = _password.value
        val bday = _birthdate.value

        viewModelScope.launch {
            _newUserUiState.value = NewUserUiState.Loading
            
            // 1. Create Auth User
            val authResult = authRepository.signUp(email, pass)
            authResult.onSuccess { user ->
                // 2. Create Firestore User
                val userResult = userRepository.createUser(user.uid, email, bday)
                userResult.onSuccess {
                    _newUserUiState.value = NewUserUiState.Created
                }.onFailure { e ->
                    _newUserUiState.value = NewUserUiState.Error("Failed to save user data: ${e.message}")
                }
            }.onFailure { e ->
                _newUserUiState.value = NewUserUiState.Error("Registration failed: ${e.message}")
            }
        }
    }
}
