package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.CreateSpecialtyDto
import com.edu.pe.automatch.data.remote.dtos.MechanicLocationDto
import com.edu.pe.automatch.data.remote.dtos.MechanicProfileDto
import com.edu.pe.automatch.data.remote.dtos.SpecialtyDto
import com.edu.pe.automatch.data.remote.dtos.UpdateMechanicProfileDto
import com.edu.pe.automatch.data.remote.dtos.UpsertMechanicLocationDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MechanicService {

    @GET("mechanics")
    suspend fun getAllMechanics(): Response<List<MechanicProfileDto>>

    @GET("mechanics/user/{userId}")
    suspend fun getMechanicByUserId(@Path("userId") userId: Long): Response<MechanicProfileDto>

    @PUT("mechanics/{id}")
    suspend fun updateMechanic(
        @Path("id") id: Long,
        @Body mechanic: UpdateMechanicProfileDto
    ): Response<MechanicProfileDto>

    // Especialidades
    @GET("mechanics/specialties")
    suspend fun getAllSpecialties(): Response<List<SpecialtyDto>>

    @POST("mechanics/specialties")
    suspend fun createSpecialty(@Body body: CreateSpecialtyDto): Response<SpecialtyDto>

    @POST("mechanics/{mechanicProfileId}/specialties/{specialtyId}")
    suspend fun addSpecialtyToMechanic(
        @Path("mechanicProfileId") mechanicProfileId: Long,
        @Path("specialtyId") specialtyId: Long
    ): Response<MechanicProfileDto>

    @DELETE("mechanics/{mechanicProfileId}/specialties/{specialtyId}")
    suspend fun removeSpecialtyFromMechanic(
        @Path("mechanicProfileId") mechanicProfileId: Long,
        @Path("specialtyId") specialtyId: Long
    ): Response<MechanicProfileDto>

    // Ubicación
    @GET("mechanic-locations/{mechanicId}")
    suspend fun getMechanicLocation(@Path("mechanicId") mechanicId: Long): Response<MechanicLocationDto>

    @POST("mechanic-locations/{mechanicId}")
    suspend fun upsertMechanicLocation(
        @Path("mechanicId") mechanicId: Long,
        @Body body: UpsertMechanicLocationDto
    ): Response<MechanicLocationDto>

    @GET("mechanic-locations")
    suspend fun getAllLocations(): Response<List<MechanicLocationDto>>
}
