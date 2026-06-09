package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.SignInRequestDto
import com.edu.pe.automatch.data.remote.dtos.SignInResponseDto
import com.edu.pe.automatch.data.remote.dtos.SignUpRequestDto
import com.edu.pe.automatch.data.remote.dtos.SignUpResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("authentication/sign-in")
    suspend fun signIn(
        @Body request: SignInRequestDto
    ): Response<SignInResponseDto>

    @POST("authentication/sign-up")
    suspend fun signUp(
        @Body request: SignUpRequestDto
    ): Response<SignUpResponseDto>

}