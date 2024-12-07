package com.example.a6starter.ui.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6starter.data.entities.LogInRequest
import com.example.a6starter.data.entities.SignUpRequest
import com.example.a6starter.data.entities.LoggedIn
import com.example.a6starter.data.model.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInScreenViewModel @Inject constructor(
    private val gymRepository: GymRepository
) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    private val _errorMessage = MutableStateFlow<String?>(null)

    private val _isLoading = MutableStateFlow(false)

    private val _user = MutableStateFlow<LoggedIn?>(null)
    val user: StateFlow<LoggedIn?> get() = _user

    fun signUp(name: String, email: String, username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val signUpRequest = SignUpRequest(name, email, username, password)
                val response = gymRepository.signUp(signUpRequest)
                if (response.isSuccessful) {
                    // Handle success
                    val signedInUser = response.body()?.firstOrNull() // Assuming we get the first user
                    if (signedInUser != null) {
                        // Optionally store the signed-in user
                        _user.value = LoggedIn(username = signedInUser.username, password = signedInUser.password)
                    } else {
                        _errorMessage.value = "Failed to sign up. No user data returned."
                    }
                } else {
                    _errorMessage.value = "Failed: ${response.message()} (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Method to update username
    fun updateUsername(newUsername: String) {
        username = newUsername
    }

    // Method to update password
    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    // Handle Login request
    fun logIn(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val logInRequest = LogInRequest(username, password)
                val response = gymRepository.logIn(logInRequest)
                if (response.isSuccessful) {
                    // Handle success
                    val loggedInUser = response.body()?.firstOrNull() // Assuming we get the first user
                    if (loggedInUser != null) {
                        // Optionally store the logged-in user
                        _user.value = loggedInUser
                    } else {
                        _errorMessage.value = "Failed to log in. No user data returned."
                    }
                } else {
                    _errorMessage.value = "Failed: ${response.message()} (${response.code()})"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

}
