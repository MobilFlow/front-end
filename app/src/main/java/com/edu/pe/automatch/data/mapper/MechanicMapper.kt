package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.remote.dtos.MechanicLocationDto
import com.edu.pe.automatch.data.remote.dtos.MechanicProfileDto
import com.edu.pe.automatch.data.remote.dtos.SpecialtyDto
import com.edu.pe.automatch.data.remote.dtos.UpdateMechanicProfileDto
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

fun SpecialtyDto.toDomain() = Specialty(id = id, name = name)

// Para PUT /mechanics/{id}: solo description, workshopName, workshopAddress
fun MechanicProfile.toUpdateDto() = UpdateMechanicProfileDto(
    description = description,
    workshopName = workshopName,
    workshopAddress = workshopAddress
)

fun MechanicLocationDto.toDomain() = MechanicLocation(
    id = id,
    mechanicId = mechanicId,
    latitude = latitude,
    longitude = longitude,
    addressText = addressText
)
