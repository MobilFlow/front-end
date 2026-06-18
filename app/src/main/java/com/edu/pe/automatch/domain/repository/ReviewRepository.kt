package com.edu.pe.automatch.domain.repository

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
}
