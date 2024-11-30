package com.example.a6starter.data.model

import com.example.a6starter.data.entities.GymEntity
import com.example.a6starter.data.remote.GymApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GymRepository @Inject constructor(
    private val gymApi: GymApi,
) {
    suspend fun getGyms(pageNumber: Int): Response<List<GymEntity>> {
        return gymApi.getGyms(pageNumber)
//        TODO(
//            "Implement this function, all you need to do here is return" +
//                    "the call from your API"
//        )
    }

}