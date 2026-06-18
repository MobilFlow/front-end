package com.edu.pe.automatch.domain.model

data class ServiceRequestInfo(
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
