package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.UpdateUserDto
import com.edu.pe.automatch.data.remote.dtos.UserDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {

    @GET("users")
    suspend fun getUsers(): Response<List<UserDto>>

    @GET("users/{userId}")
    suspend fun getUserById(
        @Path("userId") userId: Long
    ): Response<UserDto>

    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: Long,
        @Body body: UpdateUserDto
    ): Response<UserDto>
}
