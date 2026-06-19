package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.remote.dtos.MechanicLocationDto
import com.edu.pe.automatch.data.remote.dtos.MechanicProfileDto
import com.edu.pe.automatch.data.remote.dtos.SpecialtyDto
import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.Specialty

fun MechanicProfileDto.toDomain() = MechanicProfile(
    id = id,
    userId = userId,
    description = description,
    workshopName = workshopName,
    workshopAddress = workshopAddress,
    specialties = specialties.map { Specialty(id = it.id, name = it.name) }
)

fun MechanicProfile.toDto() = MechanicProfileDto(
    id = id,
    userId = userId,
    description = description,
    workshopName = workshopName,
    workshopAddress = workshopAddress,
    specialties = specialties.map { SpecialtyDto(id = it.id, name = it.name) }
)

fun MechanicLocationDto.toDomain() = MechanicLocation(
    id = id,
    mechanicId = mechanicId,
    latitude = latitude,
    longitude = longitude,
    addressText = addressText
)
