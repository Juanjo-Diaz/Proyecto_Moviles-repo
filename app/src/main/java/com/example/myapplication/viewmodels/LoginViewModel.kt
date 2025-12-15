package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private val _username = MutableLiveData("")
    private val _password = MutableLiveData("")

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

    private fun validate() {
        val u = _username.value.orEmpty()
        val p = _password.value.orEmpty()
        _isFormValid.value = u.length >= 1 && p.length >= 4
    }

    fun login(): Boolean {
        val u = _username.value.orEmpty()
        val p = _password.value.orEmpty()
        return u == "admin" && p == "1234"
    }
}
