package com.example.a6starter.ui.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.entities.LogInRequest
import com.example.a6starter.data.entities.SignUpRequest
import com.example.a6starter.data.entities.User
import com.example.a6starter.data.model.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val gymRepository: GymRepository
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    fun signIn(name: String, email: String, username: String, password: String) {
        viewModelScope.launch {
            try {
                val signUpRequest = SignUpRequest(name, email, username, password)
                val response = gymRepository.signIn(signUpRequest)
                if (response.isSuccessful) {
                    //signIn()
                } else {
                    _errorMessage.value = "Failed: ${response.message()} (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    fun logIn(username: String, password: String) {
        viewModelScope.launch {
            try {
                val logInRequest = LogInRequest(username, password)
                val response = gymRepository.logIn(logInRequest)
                if (response.isSuccessful) {
                    //logIn()
                } else {
                    _errorMessage.value = "Failed: ${response.message()} (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            }
        }
    }



    private val _username = MutableStateFlow("")
    val username: StateFlow<String> get() = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> get() = _password

    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }


}