package com.example.a6starter.data.entities

import com.squareup.moshi.Json

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class GymEntity(
    val data: List<Gym>
)

@JsonClass(generateAdapter = true)
data class Gym(
    @Json(name = "gym_id") val gymId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "location") val location: String,
    @Json(name = "type") val type: String,
    @Json(name = "created_at") val createdAt: LocalDateTime,
    @Json(name = "crowd_data") val crowdData: List<CrowdData>? = null // Associated crowd data
)

@JsonClass(generateAdapter = true)
data class CrowdData(
    @Json(name = "crowd_id") val crowdId: Int,
    @Json(name = "gym") val gym: GymSummary,
    @Json(name = "occupancy") val occupancy: Int,
    @Json(name = "percentage_full") val percentageFull: Double?,
    @Json(name = "last_updated") val lastUpdated: LocalDateTime
)

@JsonClass(generateAdapter = true)
data class GymSummary(
    @Json(name = "gym_id") val gymId: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class CrowdDataRequest(
    @Json(name = "gym") val gymId: Int,
    @Json(name = "crowd_count") val crowdCount: Int
)
