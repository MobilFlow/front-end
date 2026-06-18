package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.RatingRequestDto
import com.edu.pe.automatch.data.remote.dtos.RatingResponseDto
import com.edu.pe.automatch.data.remote.dtos.ReviewRequestDto
import com.edu.pe.automatch.data.remote.dtos.ReviewResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReputationService {

    @POST("reviews")
    suspend fun createReview(@Body request: ReviewRequestDto): Response<ReviewResponseDto>

    @POST("ratings")
    suspend fun createRating(@Body request: RatingRequestDto): Response<RatingResponseDto>
}
