package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.ServiceCategory
import com.edu.pe.automatch.domain.model.ServiceItem

interface ServiceCatalogRepository {

    suspend fun getAllServices(): List<ServiceItem>

    suspend fun searchServices(keyword: String): List<ServiceItem>

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
}
