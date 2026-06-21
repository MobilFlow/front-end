package com.edu.pe.automatch.data.repository

import android.util.Log
import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.remote.dtos.CreateCategoryDto
import com.edu.pe.automatch.data.remote.dtos.PublishServiceDto
import com.edu.pe.automatch.data.remote.services.ServiceCatalogService
import com.edu.pe.automatch.domain.model.ServiceCategory
import com.edu.pe.automatch.domain.model.ServiceItem
import com.edu.pe.automatch.domain.repository.ServiceCatalogRepository

class ServiceCatalogRepositoryImpl(
    private val serviceCatalogService: ServiceCatalogService
) : ServiceCatalogRepository {

    override suspend fun getAllServices(): List<ServiceItem> {
        val response = serviceCatalogService.getAllServices()
        Log.d("SERVICE_DEBUG", "getAllServices -> code=${response.code()}, size=${response.body()?.size}")
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun searchServices(keyword: String): List<ServiceItem> {
        val response = serviceCatalogService.searchServices(keyword)
        Log.d("SERVICE_DEBUG", "searchServices('$keyword') -> code=${response.code()}, size=${response.body()?.size}")
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun getCategories(): List<ServiceCategory> {
        val response = serviceCatalogService.getCategories()
        Log.d("SERVICE_DEBUG", "getCategories -> code=${response.code()}, size=${response.body()?.size}")
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun createCategory(name: String): ServiceCategory? {
        val response = serviceCatalogService.createCategory(CreateCategoryDto(name))
        Log.d("SERVICE_DEBUG", "createCategory('$name') -> code=${response.code()}, body=${response.body()}, error=${response.errorBody()?.string()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun publishService(
        mechanicProfileId: Long,
        title: String,
        description: String,
        priceMin: Double,
        priceMax: Double,
        categoryId: Long
    ): ServiceItem? {
        val body = PublishServiceDto(
            mechanicProfileId = mechanicProfileId,
            title = title,
            description = description,
            priceMin = priceMin,
            priceMax = priceMax,
            categoryId = categoryId
        )
        val response = serviceCatalogService.publishService(body)
        Log.d("SERVICE_DEBUG", "publishService body=$body -> code=${response.code()}, error=${response.errorBody()?.string()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
