package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.RatingRequestDto
import com.edu.pe.automatch.data.remote.dtos.RatingResponseDto
import com.edu.pe.automatch.data.remote.dtos.ReputationSummaryDto
import com.edu.pe.automatch.data.remote.dtos.ReviewRequestDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReputationService {

    @GET("reputation/mechanics/{mechanicId}/summary")
    suspend fun getSummary(
        @Path("mechanicId") mechanicId: Long
    ): Response<ReputationSummaryDto>

    @GET("reputation/mechanics/{mechanicId}/reviews")
    suspend fun getReviews(
        @Path("mechanicId") mechanicId: Long
    ): Response<List<ReviewResponseDto>>

    @GET("reputation/mechanics/{mechanicId}/ratings")
    suspend fun getRatings(
        @Path("mechanicId") mechanicId: Long
    ): Response<List<RatingResponseDto>>

    @POST("reputation/reviews")
    suspend fun createReview(
        @Body request: ReviewRequestDto
    ): Response<ReviewResponseDto>

    @POST("reputation/ratings")
    suspend fun createRating(
        @Body request: RatingRequestDto
    ): Response<RatingResponseDto>
}
