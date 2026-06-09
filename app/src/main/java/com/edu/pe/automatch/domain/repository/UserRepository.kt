package com.edu.pe.automatch.domain.repository

import kotlinx.coroutines.flow.Flow
import com.edu.pe.automatch.domain.model.User

interface UserRepository {

    suspend fun getUserById(id: Long): User?

    suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String,
        role: String
    ): User

    suspend fun signIn(
        email: String,
        password: String
    ): User

    suspend fun getCurrentUser(): User?
}