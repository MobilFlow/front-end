package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.ServiceCatalogDto
import retrofit2.Response
import retrofit2.http.GET

interface ServiceCatalogService {

    @GET("service-catalog")
    suspend fun getAllServices(): Response<List<ServiceCatalogDto>>
}
