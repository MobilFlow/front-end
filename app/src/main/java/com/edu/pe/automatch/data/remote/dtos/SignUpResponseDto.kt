package com.edu.pe.automatch.data.remote.dtos

data class SignUpResponseDto(
    val id: Long,
    val email: String,
    val fullName: String,
    val profilePicture: String?,
    val role: String
)