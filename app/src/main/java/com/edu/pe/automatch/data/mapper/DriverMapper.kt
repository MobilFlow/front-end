package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.remote.dtos.CarDto
import com.edu.pe.automatch.data.remote.dtos.DriverProfileDto
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile

fun DriverProfileDto.toDomain(cars: List<Car> = emptyList()) = DriverProfile(
    id = id,
    userId = userId,
    cars = cars
)

fun CarDto.toDomain() = Car(
    id = id,
    ownerId = ownerId,
    brand = brand,
    model = model,
    year = year,
    plate = plate,
    fuelType = fuelType
)
