package com.edu.pe.automatch.data.repository

import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.remote.dtos.CreateServiceRequestDto
import com.edu.pe.automatch.data.remote.services.ServiceRequestService
import com.edu.pe.automatch.domain.model.ServiceRequestInfo
import com.edu.pe.automatch.domain.repository.ServiceRequestRepository

class ServiceRequestRepositoryImpl(
    private val serviceRequestService: ServiceRequestService
) : ServiceRequestRepository {

    override suspend fun createServiceRequest(
        serviceId: Long?,
        driverProfileId: Long,
        mechanicProfileId: Long?,
        carId: Long?,
        description: String?,
        scheduledDate: String?
    ): ServiceRequestInfo {
        val request = CreateServiceRequestDto(
            serviceId = serviceId,
            driverProfileId = driverProfileId,
            mechanicProfileId = mechanicProfileId,
            carId = carId,
            description = description,
            scheduledDate = scheduledDate
        )
        val response = serviceRequestService.createServiceRequest(request)
        if (!response.isSuccessful) throw Exception("Error creating service request")
        return response.body()?.toDomain() ?: throw Exception("Empty response")
    }

    override suspend fun getRequestsByDriver(driverProfileId: Long): List<ServiceRequestInfo> {
        val response = serviceRequestService.getRequestsByDriver(driverProfileId)
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun getServiceHistory(driverProfileId: Long): List<ServiceRequestInfo> {
        val response = serviceRequestService.getServiceHistory(driverProfileId)
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun getServiceRequestById(serviceId: Long): ServiceRequestInfo? {
        val response = serviceRequestService.getServiceRequest(serviceId)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
    override suspend fun getRequestsByMechanic(
        mechanicProfileId: Long
    ): List<ServiceRequestInfo> {

        val response =
            serviceRequestService.getRequestsByMechanic(
                mechanicProfileId
            )

        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() }
                ?: emptyList()
        } else {
            emptyList()
        }
    }
}
