package com.edu.pe.automatch.domain.model

data class User(
    val id: Long,
    val email: String,
    val fullName: String,
    val profilePicture: String?,
    val role: String
)