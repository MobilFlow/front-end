package com.edu.pe.automatch.domain.model

data class MechanicProfile(
    val id: Long,
    val userId: Long,
    val description: String?,
    val workshopName: String?,
    val workshopAddress: String?,
    val specialties: List<Specialty>
)

data class Specialty(
    val id: Long,
    val name: String
)

data class MechanicLocation(
    val id: Long,
    val mechanicId: Long,
    val latitude: Double,
    val longitude: Double,
    val addressText: String?
)
