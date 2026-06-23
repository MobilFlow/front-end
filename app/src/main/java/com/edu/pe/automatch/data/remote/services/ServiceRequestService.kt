package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.ConfirmServiceRequestDto
import com.edu.pe.automatch.data.remote.dtos.CreateServiceRequestDto
import com.edu.pe.automatch.data.remote.dtos.ServiceRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ServiceRequestService {

    @POST("service-requests")
    suspend fun createServiceRequest(@Body request: CreateServiceRequestDto): Response<ServiceRequestDto>

    @GET("service-requests/{serviceId}")
    suspend fun getServiceRequest(@Path("serviceId") serviceId: Long): Response<ServiceRequestDto>

    @GET("service-requests/driver/{driverProfileId}")
    suspend fun getRequestsByDriver(@Path("driverProfileId") driverProfileId: Long): Response<List<ServiceRequestDto>>

    @GET("service-requests/history/{driverProfileId}")
    suspend fun getServiceHistory(@Path("driverProfileId") driverProfileId: Long): Response<List<ServiceRequestDto>>

    @PUT("service-requests/{serviceId}/confirm")
    suspend fun confirmService(
        @Path("serviceId") serviceId: Long,
        @Body body: ConfirmServiceRequestDto
    ): Response<ServiceRequestDto>

    @PUT("service-requests/{serviceId}/cancel")
    suspend fun cancelService(
        @Path("serviceId") serviceId: Long,
        @Body body: ConfirmServiceRequestDto
    ): Response<ServiceRequestDto>

    @GET("service-requests/mechanic/{mechanicProfileId}")
    suspend fun getRequestsByMechanic(
        @Path("mechanicProfileId") mechanicProfileId: Long
    ): Response<List<ServiceRequestDto>>
}
