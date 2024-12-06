package com.example.a6starter.ui.screens.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.model.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val gymRepository: GymRepository
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
            val response = gymRepository.getGyms()
            if (response.isSuccessful) {
                _gymsFlow.value = response.body()?.toList() ?: emptyList()
            } else {
                _errorMessage.value = "Failed to fetch gyms: ${response.message()} ${response.code()}"
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString());
            _errorMessage.value = "An error occurred: ${e.message}"
            e.printStackTrace()

        }
    }

}
