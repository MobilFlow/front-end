package com.edu.pe.automatch.data.repository

import com.edu.pe.automatch.data.remote.dtos.RatingRequestDto
import com.edu.pe.automatch.data.remote.dtos.ReviewRequestDto
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
        if (!response.isSuccessful) throw Exception("Error creating review")
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
        if (!response.isSuccessful) throw Exception("Error creating rating")
    }
}
