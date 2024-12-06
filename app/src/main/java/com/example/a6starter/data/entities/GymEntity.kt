package com.example.a6starter.data.entities

import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class GymEntity(
    @Json(name = "gyms") val gyms: List<Gym>
)

@JsonClass(generateAdapter = true)
data class Gym(
    @Json(name = "gym_id") val gymId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "location") val location: String,
    @Json(name = "type") val type: String,
    @Json(name = "crowd_data") val crowdData: List<CrowdData>? = null // Nullable for cases without crowd data
)

@JsonClass(generateAdapter = true)
data class CrowdData(
    @Json(name = "crowd_id") val crowdId: Int,
    @Json(name = "occupancy") val occupancy: Int,
    @Json(name = "percentage_full") val percentageFull: Double?,
    @Json(name = "last_updated") val lastUpdated: String // Using String to handle API timestamps directly
)

