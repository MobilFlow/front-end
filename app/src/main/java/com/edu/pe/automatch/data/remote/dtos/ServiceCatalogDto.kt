package com.edu.pe.automatch.data.remote.dtos

data class ServiceCatalogDto(
    val id: Long,
    val mechanicProfileId: Long?,
    val title: String?,
    val description: String?,
    val minimumPrice: Double?,
    val maximumPrice: Double?,
    val status: String?,
    val category: ServiceCategoryDto?,
    val tags: List<ServiceTagDto>?,
    val images: List<ServiceImageDto>?
)

data class ServiceCategoryDto(
    val id: Long,
    val name: String
)

data class ServiceTagDto(
    val id: Long,
    val name: String
)

data class ServiceImageDto(
    val id: Long,
    val imageUrl: String
)

// Body para POST /api/v1/services (Publish service)
data class PublishServiceDto(
    val mechanicProfileId: Long,
    val title: String,
    val description: String,
    val priceMin: Double,
    val priceMax: Double,
    val categoryId: Long
)

// GET /api/v1/categories -> [{id, name}]
data class CategoryDto(
    val id: Long,
    val name: String
)

// Body para POST /api/v1/categories
data class CreateCategoryDto(
    val name: String
)
