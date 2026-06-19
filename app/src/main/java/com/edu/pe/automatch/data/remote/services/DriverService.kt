package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.CarDto
import com.edu.pe.automatch.data.remote.dtos.CreateCarDto
import com.edu.pe.automatch.data.remote.dtos.CreateDriverProfileDto
import com.edu.pe.automatch.data.remote.dtos.DriverProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DriverService {

    @GET("drivers/user/{userId}")
    suspend fun getDriverByUserId(@Path("userId") userId: Long): Response<DriverProfileDto>

    @POST("drivers")
    suspend fun createDriverProfile(@Body request: CreateDriverProfileDto): Response<DriverProfileDto>

    @GET("drivers/{driverProfileId}/cars")
    suspend fun getCarsByDriverProfile(@Path("driverProfileId") driverProfileId: Long): Response<List<CarDto>>

    @POST("drivers/{driverProfileId}/cars")
    suspend fun createCar(@Path("driverProfileId") driverProfileId: Long, @Body request: CreateCarDto): Response<CarDto>
}
