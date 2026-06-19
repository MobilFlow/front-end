package com.edu.pe.automatch.data.remote.dtos

data class ReviewRequestDto(
    val content: String,
    val mechanicId: Long,
    val driverId: Long,
    val serviceId: Long,
    val serviceFinished: Boolean
)

data class ReviewResponseDto(
    val id: Long,
    val content: String,
    val mechanicId: Long,
    val driverId: Long,
    val serviceId: Long,
    val serviceFinished: Boolean,
    val createdAt: String? = null,
    val edited: Boolean = false
)

data class RatingRequestDto(
    val score: Int,
    val mechanicId: Long,
    val driverId: Long,
    val serviceId: Long,
    val serviceFinished: Boolean
)

data class RatingResponseDto(
    val id: Long,
    val score: Int,
    val mechanicId: Long,
    val driverId: Long,
    val serviceId: Long,
    val serviceFinished: Boolean
)

data class ReputationSummaryDto(
    val mechanicId: Long,
    val averageRating: Double,
    val totalReviews: Int
)
