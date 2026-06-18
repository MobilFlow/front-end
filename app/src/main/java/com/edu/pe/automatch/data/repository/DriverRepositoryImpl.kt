package com.edu.pe.automatch.data.repository

import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.remote.services.DriverService
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile
import com.edu.pe.automatch.domain.repository.DriverRepository

class DriverRepositoryImpl(
    private val driverService: DriverService
) : DriverRepository {

    override suspend fun getDriverByUserId(userId: Long): DriverProfile? {
        val response = driverService.getDriverByUserId(userId)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun getCarsByDriverProfile(driverProfileId: Long): List<Car> {
        val response = driverService.getCarsByDriverProfile(driverProfileId)
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }
}
