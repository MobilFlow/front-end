package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.remote.dtos.ServiceRequestDto
import com.edu.pe.automatch.domain.model.ServiceRequestInfo

fun ServiceRequestDto.toDomain() = ServiceRequestInfo(
    id = id,
    serviceId = serviceId,
    driverProfileId = driverProfileId,
    mechanicProfileId = mechanicProfileId,
    carId = carId,
    description = description,
    scheduledDate = scheduledDate,
    status = status,
    driverConfirmed = driverConfirmed,
    mechanicConfirmed = mechanicConfirmed,
    completedAt = completedAt,
    createdAt = createdAt
)
