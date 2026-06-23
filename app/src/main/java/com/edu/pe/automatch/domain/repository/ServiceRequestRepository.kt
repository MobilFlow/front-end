package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.ServiceRequestInfo

interface ServiceRequestRepository {

    suspend fun createServiceRequest(
        serviceId: Long?,
        driverProfileId: Long,
        mechanicProfileId: Long?,
        carId: Long?,
        description: String?,
        scheduledDate: String?
    ): ServiceRequestInfo

    suspend fun getRequestsByDriver(driverProfileId: Long): List<ServiceRequestInfo>

    suspend fun getServiceHistory(driverProfileId: Long): List<ServiceRequestInfo>

    suspend fun getServiceRequestById(serviceId: Long): ServiceRequestInfo?

    suspend fun getRequestsByMechanic(mechanicProfileId: Long): List<ServiceRequestInfo>

    suspend fun confirmService(requestId: Long, actorProfileId: Long, role: String): ServiceRequestInfo?

    suspend fun cancelService(requestId: Long, actorProfileId: Long, role: String): ServiceRequestInfo?
}
