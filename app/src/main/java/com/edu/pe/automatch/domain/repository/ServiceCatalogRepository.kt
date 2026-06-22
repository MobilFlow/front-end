package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.ServiceCategory
import com.edu.pe.automatch.domain.model.ServiceItem

interface ServiceCatalogRepository {

    suspend fun getAllServices(): List<ServiceItem>

    suspend fun searchServices(keyword: String): List<ServiceItem>

    suspend fun getServicesByMechanic(mechanicProfileId: Long): List<ServiceItem>

    suspend fun getCategories(): List<ServiceCategory>

    suspend fun createCategory(name: String): ServiceCategory?

    suspend fun publishService(
        mechanicProfileId: Long,
        title: String,
        description: String,
        priceMin: Double,
        priceMax: Double,
        categoryId: Long
    ): ServiceItem?

    suspend fun updateService(
        serviceId: Long,
        title: String,
        description: String,
        priceMin: Double,
        priceMax: Double,
        categoryId: Long
    ): ServiceItem?

    suspend fun setServiceActive(serviceId: Long, active: Boolean): ServiceItem?

    suspend fun deleteService(serviceId: Long): Boolean
}
