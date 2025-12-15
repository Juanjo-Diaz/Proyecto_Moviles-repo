package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    private val _username = MutableLiveData("")
    private val _password = MutableLiveData("")
    private val _confirmPassword = MutableLiveData("")
    private val _birthdate = MutableLiveData("")

    private val _isFormValid = MutableLiveData(false)
    val isFormValid: LiveData<Boolean> = _isFormValid

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
        val u = _username.value.orEmpty()
        val p = _password.value.orEmpty()
        val c = _confirmPassword.value.orEmpty()
        _isFormValid.value = (u.length >= 1 && p.length >= 4 && c.length >= 4)
    }

    fun passwordsMatch(): Boolean = _password.value == _confirmPassword.value
}
