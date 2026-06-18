package com.edu.pe.automatch.data.remote.dtos

data class ServiceCatalogDto(
    val id: Long,
    val name: String,
    val description: String?,
    val estimatedPrice: Double?
)
