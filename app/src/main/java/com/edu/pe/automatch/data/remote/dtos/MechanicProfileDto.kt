package com.edu.pe.automatch.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class MechanicProfileDto(
    val id: Long,
    val userId: Long,
    @SerializedName("bio")
    val description: String?,
    val workshopName: String?,
    val workshopAddress: String?,
    val specialties: List<SpecialtyDto>
)

data class SpecialtyDto(
    val id: Long,
    val name: String
)

data class MechanicLocationDto(
    val id: Long,
    val mechanicId: Long,
    val latitude: Double,
    val longitude: Double,
    val addressText: String?
)

data class UpdateMechanicProfileDto(
    val description: String?,
    val workshopName: String?,
    val workshopAddress: String?
)

data class CreateSpecialtyDto(
    val name: String
)

data class UpsertMechanicLocationDto(
    val latitude: Double,
    val longitude: Double,
    val addressText: String?
)
