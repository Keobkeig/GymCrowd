package com.example.a6starter.data.model


import android.util.Log
import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.entities.LogInRequest
import com.example.a6starter.data.entities.LoggedIn
import com.example.a6starter.data.entities.SignUpRequest
import com.example.a6starter.data.entities.SignedIn
import com.example.a6starter.data.remote.GymApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GymRepository @Inject constructor(
    private val gymApi: GymApi,
) {
    suspend fun getGyms(): Response<List<Gym>> {
        return gymApi.getGyms();
    }

    suspend fun signUp(signUpRequest: SignUpRequest): Response<List<SignedIn>> {
        return gymApi.signUp(signUpRequest)
    }

    suspend fun logIn(logInRequest: LogInRequest): Response<List<LoggedIn>> {
        return gymApi.logIn(logInRequest)
    }

    // Define the function to fetch exercises for a specific page
    suspend fun getExercises(page: Int): Response<List<Exercise>> {
        // Calculate the starting and ending exercise IDs based on the page number
        val startId = (page - 1) * 20
        val endId = page * 20

        // Create a list to hold the fetched exercises
        val exercises = mutableListOf<Exercise>()

        // Loop through the ID range and fetch each exercise
        for (exerciseId in startId..endId) {
            // Fetch the exercise for the current ID
            val response = gymApi.getSpecificExercise(exerciseId)

            // Log the response code and body for debugging
            Log.d("API Debug", "Fetching exercise ID: $exerciseId")
            Log.d("API Debug", "Response code: ${response.code()}")
            Log.d("API Debug", "Response body: ${response.body()}")

            // If the response is successful, add the exercise to the list
            if (response.isSuccessful) {
                response.body()?.let {
                    exercises.add(it)
                }
            } else {
                // Handle the error case (for example, log or notify the user)
                Log.e("API Error", "Failed to fetch exercise with ID: $exerciseId")
            }
        }

        // Return the list of exercises
        return Response.success(exercises)
    }
}