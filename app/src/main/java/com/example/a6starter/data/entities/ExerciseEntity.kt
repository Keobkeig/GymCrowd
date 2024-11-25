package com.example.a6starter.data.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class Exercise(
    @Json(name = "exercise_id") val exerciseId: Int,
    @Json(name = "name") val name: String,
    @Json(name = "body_part") val bodyPart: String,
    @Json(name = "equipment") val equipment: String?,
    @Json(name = "gif_url") val gifUrl: String?,
    @Json(name = "target") val target: String,
    @Json(name = "secondary_muscles") val secondaryMuscles: String?,
    @Json(name = "instructions") val instructions: String
)

@JsonClass(generateAdapter = true)
data class UserWorkout(
    @Json(name = "workout_id") val workoutId: Int,
    @Json(name = "user") val user: UserSummary,
    @Json(name = "date") val date: LocalDate,
    @Json(name = "created_at") val createdAt: LocalDateTime,
    @Json(name = "workout_exercises") val workoutExercises: List<WorkoutExercise>? = null // Associated exercises
)

@JsonClass(generateAdapter = true)
data class WorkoutExercise(
    @Json(name = "entry_id") val entryId: Int,
    @Json(name = "workout") val workout: WorkoutSummary,
    @Json(name = "exercise") val exercise: ExerciseSummary,
    @Json(name = "sets") val sets: Int,
    @Json(name = "reps") val reps: Int,
    @Json(name = "weight") val weight: Double?
)

@JsonClass(generateAdapter = true)
data class ExerciseSummary(
    @Json(name = "exercise_id") val exerciseId: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class WorkoutSummary(
    @Json(name = "workout_id") val workoutId: Int,
    @Json(name = "date") val date: LocalDate
)

