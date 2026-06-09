package com.edu.pe.automatch.data.remote.dtos

data class SignUpRequestDto(
    val email: String,
    val password: String,
    val fullName: String,
    val phoneNumber: String,
    val role: String
)