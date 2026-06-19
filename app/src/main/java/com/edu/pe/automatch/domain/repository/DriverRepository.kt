package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile

interface DriverRepository {

    suspend fun getDriverProfileById(id: Long): DriverProfile?

    suspend fun getDriverByUserId(userId: Long): DriverProfile?

    suspend fun createDriverProfile(userId: Long): DriverProfile

    suspend fun getCarsByDriverProfile(driverProfileId: Long): List<Car>

    suspend fun createCar(driverProfileId: Long, brand: String, model: String, year: Int, plate: String, fuelType: String? = null): Car
}
