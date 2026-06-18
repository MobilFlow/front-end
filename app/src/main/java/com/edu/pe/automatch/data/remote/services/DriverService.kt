package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.CarDto
import com.edu.pe.automatch.data.remote.dtos.DriverProfileDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DriverService {

    @GET("driver-profiles/user/{userId}")
    suspend fun getDriverByUserId(@Path("userId") userId: Long): Response<DriverProfileDto>

    @GET("cars/owner/{driverProfileId}")
    suspend fun getCarsByDriverProfile(@Path("driverProfileId") driverProfileId: Long): Response<List<CarDto>>
}
