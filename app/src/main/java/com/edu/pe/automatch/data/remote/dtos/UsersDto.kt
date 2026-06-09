package com.edu.pe.automatch.data.remote.dtos


data class UserDto(
    val id: Long,
    val email: String,
    val fullName: String,
    val profilePicture: String?,
    val role: String
)