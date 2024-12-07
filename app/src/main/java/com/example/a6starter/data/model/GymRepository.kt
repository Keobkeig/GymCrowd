package com.example.a6starter.data.model


import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.entities.Gym
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

    suspend fun getExercises(): Response<List<Exercise>> {
        return gymApi.getExercises()
    }

}