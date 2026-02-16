package com.example.p2dam_226.viewmodels

sealed interface NewUserUiState {
    object Idle : NewUserUiState
    object Loading : NewUserUiState
    object Created : NewUserUiState
    data class Error(val message: String) : NewUserUiState
}
