package com.edu.pe.automatch.data.remote.dtos

data class UpdateUserDto(
    val name: String,
    val email: String,
    val profilePicture: String?
)
