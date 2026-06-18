package com.edu.pe.automatch.domain.model

data class DriverProfile(
    val id: Long,
    val userId: Long,
    val cars: List<Car>
)

data class Car(
    val id: Long,
    val ownerId: Long,
    val brand: String,
    val model: String,
    val year: Int,
    val plate: String,
    val fuelType: String?
)
