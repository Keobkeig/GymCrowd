package com.example.a6starter.data.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Gym(
    @Json(name = "gym_id") val gymId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "location") val location: String,
    @Json(name = "type") val type: String,
    @Json(name = "crowd_data") val crowdData: List<CrowdData>? = null
)

@JsonClass(generateAdapter = true)
data class CrowdData(
    @Json(name = "crowd_id") val crowdId: Int,
    @Json(name = "gym") val gym: Int,
    @Json(name = "occupancy") val occupancy: Int,
    @Json(name = "percentage_full") val percentageFull: Double?,
    @Json(name = "last_updated") val lastUpdated: String
)
