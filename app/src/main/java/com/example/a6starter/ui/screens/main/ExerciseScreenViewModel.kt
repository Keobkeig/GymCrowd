package com.example.a6starter.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.model.GymRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ExerciseScreenViewState(
    val favorites: List<Exercise>,
    val allExercises: List<Exercise>,
    val isLoading: Boolean
)

@HiltViewModel
class ExerciseScreenViewModel @Inject constructor(
    private val gymRepository: GymRepository
) : ViewModel() {

    private val favoritesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    private val allExercisesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    private val isLoadingFlow = MutableStateFlow(false)

    val mainScreenViewState: StateFlow<ExerciseScreenViewState> =
        combine(favoritesFlow, allExercisesFlow, isLoadingFlow) { favorites, allExercises, isLoading ->
            ExerciseScreenViewState(
                favorites = favorites,
                allExercises = allExercises,
                isLoading = isLoading
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ExerciseScreenViewState(emptyList(), emptyList(), false))

    init {
        loadExercises()
    }

    fun addFavoriteExercise(exerciseId: Int) {
        // Find the exercise in allExercises
        val exercise = allExercisesFlow.value.find { it.exerciseId == exerciseId }
        exercise?.let { ex ->
            // Add to favorites
            favoritesFlow.update { currentFavorites ->
                currentFavorites + ex
            }
            // Remove from all exercises
            allExercisesFlow.update { currentAllExercises ->
                currentAllExercises.filterNot { it.exerciseId == exerciseId }
            }
        }
    }

    fun removeFavoriteExercise(exerciseId: Int) {
        // Find the exercise in favorites
        val exercise = favoritesFlow.value.find { it.exerciseId == exerciseId }
        exercise?.let { ex ->
            // Remove from favorites
            favoritesFlow.update { currentFavorites ->
                currentFavorites.filterNot { it.exerciseId == exerciseId }
            }
            // Add back to all exercises
            allExercisesFlow.update { currentAllExercises ->
                currentAllExercises + ex
            }
        }
    }

    fun loadExercises() = viewModelScope.launch {
        if (isLoadingFlow.value) return@launch
        isLoadingFlow.value = true

        try {
            val response = gymRepository.getExercises()
            if (response.isSuccessful) {
                response.body()?.let { newExercises ->
                    // Only add exercises that aren't already in favorites
                    val exercisesToAdd = newExercises.filterNot { newExercise ->
                        favoritesFlow.value.any { it.exerciseId == newExercise.exerciseId }
                    }
                    allExercisesFlow.update { currentExercises ->
                        currentExercises + exercisesToAdd
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoadingFlow.value = false
        }
    }
}
