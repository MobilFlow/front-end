package com.edu.pe.automatch.domain.model

data class ServiceItem(
    val id: Long,
    val mechanicProfileId: Long?,
    val title: String,
    val description: String,
    val minimumPrice: Double?,
    val maximumPrice: Double?,
    val status: String?,
    val categoryName: String?,
    val imageUrl: String?
)
