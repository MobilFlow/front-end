package com.edu.pe.automatch.data.repository

import android.util.Log
import com.edu.pe.automatch.data.local.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.edu.pe.automatch.data.local.dao.UserDao
import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.mapper.toEntity
import com.edu.pe.automatch.data.remote.dtos.SignInRequestDto
import com.edu.pe.automatch.data.remote.dtos.SignUpRequestDto
import com.edu.pe.automatch.data.remote.services.AuthService
import com.edu.pe.automatch.data.remote.services.UserService
import com.edu.pe.automatch.domain.repository.UserRepository
import com.edu.pe.automatch.domain.model.User

class UserRepositoryImpl(
    private val userService: UserService,
    private val authService: AuthService,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : UserRepository {
    override suspend fun getUserById(id: Long): User? {
        val response = userService.getUserById(id)
        if (!response.isSuccessful) return null
        return response.body()?.toDomain()
    }

    override suspend fun signUp(
        email: String,
        password: String,
        fullName: String,
        phoneNumber: String,
        role: String
    ): User = withContext(Dispatchers.IO) {
        val request = SignUpRequestDto(
            email = email,
            password = password,
            fullName = fullName,
            phoneNumber = phoneNumber,
            role = role
        )

        val response = authService.signUp(request)

        if (response.isSuccessful) {
            val dto = response.body()
            val createdUser = dto?.toDomain()

            createdUser?.let {
                userDao.deleteAll() // LIMPIAR SESIONES ANTERIORES
                userDao.insert(dto.toEntity())
            }

            createdUser ?: throw Exception("Error: respuesta vacía al registrar usuario")
        } else {
            throw Exception("Error in Sign Up: ${response.code()}")
        }
    }

    override suspend fun signIn(
        email: String,
        password: String
    ): User {
        val response = authService.signIn(
            SignInRequestDto(
                email = email,
                password = password
            )
        )

        if (!response.isSuccessful) throw Exception("Invalid credentials")

        val body = response.body() ?: throw Exception("Empty response")
        SessionManager.token = body.token

        val user = getUserById(body.id) ?: throw Exception("User not found")

        userDao.deleteAll() // LIMPIAR SESIONES ANTERIORES
        userDao.insert(user.toEntity())

        return user
    }

    override suspend fun getCurrentUser(): User? {
        return userDao.getCurrentUser()?.toDomain()
    }

    override suspend fun updateProfile(fullName: String?, profilePicture: String?): User? {
        val entity = userDao.getCurrentUser() ?: return null
        val updated = entity.copy(
            fullName = fullName?.takeIf { it.isNotBlank() } ?: entity.fullName,
            profilePicture = profilePicture?.takeIf { it.isNotBlank() }
        )
        userDao.insert(updated)
        return updated.toDomain()
    }
}
