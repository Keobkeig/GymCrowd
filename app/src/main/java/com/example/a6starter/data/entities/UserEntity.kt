package com.example.a6starter.data.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "username") val username: String,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "created_at") val createdAt: LocalDateTime,
    @Json(name = "is_active") val isActive: Boolean,
    @Json(name = "is_staff") val isStaff: Boolean,
    @Json(name = "is_superuser") val isSuperuser: Boolean,
    @Json(name = "preferences") val preferences: List<UserPreference>? = null // Associated preferences
)

@JsonClass(generateAdapter = true)
data class UserPreference(
    @Json(name = "preference_id") val preferenceId: Int,
    @Json(name = "user") val user: UserSummary,
    @Json(name = "max_crowd_level") val maxCrowdLevel: Double,
    @Json(name = "created_at") val createdAt: LocalDateTime
)

@JsonClass(generateAdapter = true)
data class UserSummary(
    @Json(name = "user_id") val userId: Int,
    @Json(name = "username") val username: String,
    @Json(name = "name") val name: String
)
