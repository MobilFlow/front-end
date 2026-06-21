package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.remote.dtos.ServiceCatalogDto
import com.edu.pe.automatch.domain.model.ServiceItem

fun ServiceCatalogDto.toDomain() = ServiceItem(
    id = id,
    mechanicProfileId = mechanicProfileId,
    title = title.orEmpty(),
    description = description.orEmpty(),
    minimumPrice = minimumPrice,
    maximumPrice = maximumPrice,
    status = status,
    categoryName = category?.name,
    imageUrl = images?.firstOrNull()?.imageUrl
)

fun com.edu.pe.automatch.data.remote.dtos.CategoryDto.toDomain() =
    com.edu.pe.automatch.domain.model.ServiceCategory(id = id, name = name)
