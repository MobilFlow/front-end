package com.edu.pe.automatch.data.remote.dtos

data class SignInResponseDto(
    val id: Long,
    val email: String,
    val token: String
)