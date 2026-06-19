package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.data.remote.dtos.RatingResponseDto
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto

interface ReviewRepository {

    suspend fun createReview(
        content: String,
        mechanicId: Long,
        driverId: Long,
        serviceId: Long,
        serviceFinished: Boolean
    )

    suspend fun createRating(
        score: Int,
        mechanicId: Long,
        driverId: Long,
        serviceId: Long,
        serviceFinished: Boolean
    )

    suspend fun getReputationSummary(mechanicId: Long): ReputationSummaryDto?

    suspend fun getMechanicReviews(mechanicId: Long): List<ReviewResponseDto>

    suspend fun getRatings(mechanicId: Long): List<RatingResponseDto>
}
