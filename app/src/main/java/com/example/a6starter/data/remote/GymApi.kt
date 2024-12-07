package com.example.a6starter.data.remote

import com.example.a6starter.data.entities.CrowdData
import com.example.a6starter.data.entities.Exercise
import com.example.a6starter.data.entities.Gym
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface GymApi {
    // TODO specify your API
    //  This time you will want your function to take in a parameter for the page number, since we
    //  are dealing with paginated data.
    //  Annotate the parameter with @Query("page[number]") to tell Retrofit how to put the parameter
    //  in the API Request.

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
}

