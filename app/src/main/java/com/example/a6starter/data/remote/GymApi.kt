package com.example.a6starter.data.remote

import com.example.a6starter.data.entities.CrowdData
import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.entities.Gym
import com.example.a6starter.data.entities.LogInRequest
import com.example.a6starter.data.entities.LoggedIn
import com.example.a6starter.data.entities.SignUpRequest
import com.example.a6starter.data.entities.SignedIn
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GymApi {
    // Get a list of gyms
    @GET("gyms/")
    suspend fun getGyms(): Response<List<Gym>>

    // Get details of a specific gym
    @GET("gyms/{id}/")
    suspend fun getGymDetails(
        @Path("id") gymId: Int
    ): Response<Gym>

    // Get a list of crowd data
    @GET("crowd-data/")
    suspend fun getCrowdData(): Response<List<CrowdData>>

    // Create a new crowd data entry
    @POST("crowd-data/")
    suspend fun createCrowdData(
//        @Body crowdDataRequest: CrowdDataRequest
    ): Response<CrowdData>

    @POST("users/login/")
    suspend fun logIn(
        @Body requestBody: LogInRequest
    ): Response<List<LoggedIn>>

    @POST("users/signup/")
    suspend fun signUp(
        @Body requestBody: SignUpRequest
    ): Response<List<SignedIn>>

    // Get details of specific crowd data
    @GET("crowd-data/{id}/")
    suspend fun getCrowdDataDetails(
        @Path("id") crowdDataId: Int
    ): Response<CrowdData>

    // Update specific crowd data
    @PUT("crowd-data/{id}/")
    suspend fun updateCrowdData(
        @Path("id") crowdDataId: Int,
//        @Body crowdDataRequest: CrowdDataRequest
    ): Response<CrowdData>

    // Delete specific crowd data
    @DELETE("crowd-data/{id}/")
    suspend fun deleteCrowdData(
        @Path("id") crowdDataId: Int
    ): Response<Unit>


    @GET("workouts/exercise/")
    suspend fun getExercises(): Response<List<Exercise>>

    @GET("workouts/exercises/{id}/")
    suspend fun getSpecificExercise(
        @Path("id") exerciseId: Int
    ): Response<Exercise>

}

