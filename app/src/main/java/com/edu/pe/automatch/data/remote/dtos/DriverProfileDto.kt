package com.edu.pe.automatch.data.remote.dtos

data class DriverProfileDto(
    val id: Long,
    val userId: Long
)

data class CarDto(
    val id: Long,
    val ownerId: Long,
    val brand: String,
    val model: String,
    val year: Int,
    val plate: String,
    val fuelType: String?
)

data class CreateCarDto(
    val brand: String,
    val model: String,
    val year: Int,
    val plate: String,
    val fuelType: String? = null
)

data class CreateDriverProfileDto(
    val userId: Long,
    val licenseNumber: String = ""
)
