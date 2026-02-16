package com.example.p2dam_226.viewmodels

sealed interface UserUiState {
    object Idle : UserUiState
    object Loading : UserUiState
    object Authenticated : UserUiState
    data class Error(val message: String) : UserUiState
}
