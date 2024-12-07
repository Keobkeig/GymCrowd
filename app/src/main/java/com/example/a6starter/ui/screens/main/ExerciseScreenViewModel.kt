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
)@HiltViewModel
class ExerciseScreenViewModel @Inject constructor(
    private val gymRepository: GymRepository
) : ViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
    }

    private val favoritesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    private val allExercisesFlow = MutableStateFlow<List<Exercise>>(emptyList())
    private val isLoadingFlow = MutableStateFlow(false)
    private var currentPage = 1
    private var hasMoreData = true

    val exerciseScreenViewState: StateFlow<ExerciseScreenViewState> =
        combine(favoritesFlow, allExercisesFlow, isLoadingFlow) { favorites, allExercises, isLoading ->
            ExerciseScreenViewState(
                favorites = favorites,
                allExercises = allExercises,
                isLoading = isLoading
            )
        }.stateIn(viewModelScope, SharingStarted.Eagerly, ExerciseScreenViewState(emptyList(), emptyList(), false))

    init {
        loadNextPage()
    }

    fun addFavoriteExercise(exerciseId: Int) {
        val exercise = allExercisesFlow.value.find { it.exerciseId == exerciseId }
        exercise?.let { ex ->
            favoritesFlow.update { currentFavorites ->
                currentFavorites + ex
            }
            allExercisesFlow.update { currentAllExercises ->
                currentAllExercises.filterNot { it.exerciseId == exerciseId }
            }
        }
    }

    fun removeFavoriteExercise(exerciseId: Int) {
        val exercise = favoritesFlow.value.find { it.exerciseId == exerciseId }
        exercise?.let { ex ->
            favoritesFlow.update { currentFavorites ->
                currentFavorites.filterNot { it.exerciseId == exerciseId }
            }
            allExercisesFlow.update { currentAllExercises ->
                currentAllExercises + ex
            }
        }
    }

    // Updated loadNextPage to use the new getExercises() method
    fun loadNextPage() = viewModelScope.launch {
        if (isLoadingFlow.value || !hasMoreData) return@launch
        isLoadingFlow.value = true

        try {
            // Fetch exercises for the current page
            val response = gymRepository.getExercises(page = currentPage)
            if (response.isSuccessful) {
                response.body()?.let { newExercises ->
                    if (newExercises.size < PAGE_SIZE) {
                        hasMoreData = false // No more pages
                    }

                    // Filter out exercises that are already in favorites or allExercises
                    val exercisesToAdd = newExercises.filterNot { newExercise ->
                        favoritesFlow.value.any { it.exerciseId == newExercise.exerciseId } ||
                                allExercisesFlow.value.any { it.exerciseId == newExercise.exerciseId }
                    }

                    // Update allExercisesFlow with the new exercises
                    allExercisesFlow.update { currentExercises ->
                        currentExercises + exercisesToAdd
                    }

                    // Increment the page for the next load
                    currentPage++
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoadingFlow.value = false
        }
    }
}
