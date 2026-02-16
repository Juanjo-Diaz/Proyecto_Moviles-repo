package com.example.p2dam_226.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p2dam_226.firebase.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _userUiState = MutableStateFlow<UserUiState>(UserUiState.Idle)
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

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

    private fun validate() {
        val u = _username.value
        val p = _password.value
        _isFormValid.value = u.isNotEmpty() && p.length >= 6
    }

    fun login() {
        viewModelScope.launch {
            _userUiState.value = UserUiState.Loading
            val result = repository.signIn(_username.value, _password.value)
            result.onSuccess { user ->
                _userUiState.value = UserUiState.Authenticated
            }.onFailure {
                _userUiState.value = UserUiState.Error(it.message ?: "Login failed")
            }
        }
    }

    fun resetState() {
        _userUiState.value = UserUiState.Idle
    }
}
