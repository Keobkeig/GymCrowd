package com.example.a6starter.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.remote.GymApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val gymApi: GymApi
) : ViewModel() {

    private val _gymsFlow = MutableStateFlow<List<Gym>>(emptyList())
    val gymsFlow: StateFlow<List<Gym>> get() = _gymsFlow

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        fetchGyms()
    }

    private fun fetchGyms() = viewModelScope.launch {
        try {
            val response = gymApi.getGyms()
            if (response.isSuccessful) {
                _gymsFlow.value = response.body()?.gyms ?: emptyList()
            } else {
                _errorMessage.value = "Failed to fetch gyms: ${response.message()}"
            }
        } catch (e: Exception) {
            _errorMessage.value = "An error occurred: ${e.message}"
        }
    }

}
