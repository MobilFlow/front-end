package com.edu.pe.automatch.data.remote.services

import com.edu.pe.automatch.data.remote.dtos.CategoryDto
import com.edu.pe.automatch.data.remote.dtos.CreateCategoryDto
import com.edu.pe.automatch.data.remote.dtos.PublishServiceDto
import com.edu.pe.automatch.data.remote.dtos.ServiceCatalogDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceCatalogService {

    @GET("services")
    suspend fun getAllServices(): Response<List<ServiceCatalogDto>>

    @GET("search")
    suspend fun searchServices(@Query("keyword") keyword: String): Response<List<ServiceCatalogDto>>

    @POST("services")
    suspend fun publishService(@Body body: PublishServiceDto): Response<ServiceCatalogDto>

    @GET("categories")
    suspend fun getCategories(): Response<List<CategoryDto>>

    @POST("categories")
    suspend fun createCategory(@Body body: CreateCategoryDto): Response<CategoryDto>
}
