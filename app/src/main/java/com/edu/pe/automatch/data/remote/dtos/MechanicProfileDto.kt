package com.edu.pe.automatch.data.remote.dtos

data class MechanicProfileDto(
    val id: Long,
    val userId: Long,
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
