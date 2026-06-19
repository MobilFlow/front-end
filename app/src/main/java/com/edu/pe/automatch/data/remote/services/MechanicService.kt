package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.MechanicLocationDto
import com.edu.pe.automatch.data.remote.dtos.MechanicProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface MechanicService {

    @GET("mechanics")
    suspend fun getAllMechanics(): Response<List<MechanicProfileDto>>

    @GET("mechanics/user/{userId}")
    suspend fun getMechanicByUserId(@Path("userId") userId: Long): Response<MechanicProfileDto>

    @PUT("mechanics/{id}")
    suspend fun updateMechanic(@Path("id") id: Long, @Body mechanic: MechanicProfileDto): Response<MechanicProfileDto>

    @GET("mechanics/locations")
    suspend fun getAllLocations(): Response<List<MechanicLocationDto>>

    @GET("mechanic-locations/{mechanicId}")
    suspend fun getMechanicLocation(@Path("mechanicId") mechanicId: Long): Response<MechanicLocationDto>
}
