package com.edu.pe.automatch.data.repository

import android.util.Log
import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.remote.dtos.CreateCarDto
import com.edu.pe.automatch.data.remote.dtos.CreateDriverProfileDto
import com.edu.pe.automatch.data.remote.services.DriverService
import com.edu.pe.automatch.domain.model.Car
import com.edu.pe.automatch.domain.model.DriverProfile
import com.edu.pe.automatch.domain.repository.DriverRepository

class DriverRepositoryImpl(
    private val driverService: DriverService
) : DriverRepository {

    override suspend fun getDriverByUserId(userId: Long): DriverProfile? {
        val response = driverService.getDriverByUserId(userId)
        Log.d("DRIVER_DEBUG", "getDriverByUserId($userId) -> code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun createDriverProfile(userId: Long): DriverProfile {
        val response = driverService.createDriverProfile(CreateDriverProfileDto(userId = userId))
        Log.d("DRIVER_DEBUG", "createDriverProfile($userId) -> code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
        if (!response.isSuccessful) throw Exception("Error creating driver profile: code=${response.code()}")
        return response.body()?.toDomain() ?: throw Exception("Empty response creating driver profile")
    }

    override suspend fun getCarsByDriverProfile(driverProfileId: Long): List<Car> {
        val response = driverService.getCarsByDriverProfile(driverProfileId)
        Log.d("DRIVER_DEBUG", "getCarsByDriverProfile($driverProfileId) -> code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun createCar(driverProfileId: Long, brand: String, model: String, year: Int, plate: String, fuelType: String?): Car {
        val request = CreateCarDto(
            brand = brand,
            model = model,
            year = year,
            plate = plate,
            fuelType = fuelType
        )
        Log.d("DRIVER_DEBUG", "createCar(driverProfileId=$driverProfileId) body=$request")
        val response = driverService.createCar(driverProfileId, request)
        Log.d("DRIVER_DEBUG", "createCar -> code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
        if (!response.isSuccessful) throw Exception("Error creating car: code=${response.code()}")
        return response.body()?.toDomain() ?: throw Exception("Empty response")
    }
}
