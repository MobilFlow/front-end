package com.edu.pe.automatch.data.remote.dtos

data class ServiceRequestDto(
    val id: Long,
    val serviceId: Long?,
    val driverProfileId: Long,
    val mechanicProfileId: Long?,
    val carId: Long?,
    val description: String?,
    val scheduledDate: String?,
    val status: String?,
    val driverConfirmed: Boolean?,
    val mechanicConfirmed: Boolean?,
    val completedAt: String?,
    val createdAt: String?
)

data class CreateServiceRequestDto(
    val serviceId: Long?,
    val driverProfileId: Long,
    val mechanicProfileId: Long?,
    val carId: Long?,
    val description: String?,
    val scheduledDate: String?
)

data class ConfirmServiceRequestDto(
    val actorProfileId: Long,
    val role: String
)
