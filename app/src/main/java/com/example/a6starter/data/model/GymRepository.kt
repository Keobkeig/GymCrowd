package com.example.a6starter.data.model


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

    suspend fun signIn(signUpRequest: SignUpRequest): Response<List<SignedIn>> {
        return gymApi.signIn(signUpRequest)
    }

    suspend fun logIn(logInRequest: LogInRequest): Response<List<LoggedIn>> {
        return gymApi.logIn(logInRequest)
    }
}