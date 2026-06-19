package com.edu.pe.automatch.data.repository

import com.edu.pe.automatch.data.remote.dtos.RatingRequestDto
import com.edu.pe.automatch.data.remote.dtos.RatingResponseDto
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.data.remote.dtos.ReviewRequestDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import com.edu.pe.automatch.data.remote.services.ReputationService
import com.edu.pe.automatch.domain.repository.ReviewRepository

class ReviewRepositoryImpl(
    private val reputationService: ReputationService
) : ReviewRepository {

    override suspend fun createReview(
        content: String,
        mechanicId: Long,
        driverId: Long,
        serviceId: Long,
        serviceFinished: Boolean
    ) {
        val request = ReviewRequestDto(
            content = content,
            mechanicId = mechanicId,
            driverId = driverId,
            serviceId = serviceId,
            serviceFinished = serviceFinished
        )

        val response = reputationService.createReview(request)

        if (!response.isSuccessful) {
            throw Exception("Error creating review")
        }
    }

    override suspend fun createRating(
        score: Int,
        mechanicId: Long,
        driverId: Long,
        serviceId: Long,
        serviceFinished: Boolean
    ) {
        val request = RatingRequestDto(
            score = score,
            mechanicId = mechanicId,
            driverId = driverId,
            serviceId = serviceId,
            serviceFinished = serviceFinished
        )

        val response = reputationService.createRating(request)

        if (!response.isSuccessful) {
            throw Exception("Error creating rating")
        }
    }

    override suspend fun getReputationSummary(mechanicId: Long): ReputationSummaryDto? {
        val response = reputationService.getSummary(mechanicId)
        return if (response.isSuccessful) response.body() else null
    }

    override suspend fun getMechanicReviews(mechanicId: Long): List<ReviewResponseDto> {
        val response = reputationService.getReviews(mechanicId)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }

    override suspend fun getRatings(mechanicId: Long): List<RatingResponseDto> {
        val response = reputationService.getRatings(mechanicId)
        return if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
    }
}
