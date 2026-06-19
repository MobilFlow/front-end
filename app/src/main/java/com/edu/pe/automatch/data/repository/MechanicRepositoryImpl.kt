package com.edu.pe.automatch.data.repository

import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.mapper.toDto
import com.edu.pe.automatch.data.remote.services.MechanicService
import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.repository.MechanicRepository

class MechanicRepositoryImpl(
    private val mechanicService: MechanicService
) : MechanicRepository {

    override suspend fun getAllMechanics(): List<MechanicProfile> {
        val response = mechanicService.getAllMechanics()
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun getMechanicByUserId(userId: Long): MechanicProfile? {
        val response = mechanicService.getMechanicByUserId(userId)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun updateMechanic(mechanic: MechanicProfile): Boolean {
        val response = mechanicService.updateMechanic(mechanic.id, mechanic.toDto())
        return response.isSuccessful
    }

    override suspend fun getAllLocations(): List<MechanicLocation> {
        val response = mechanicService.getAllLocations()
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun getMechanicLocation(mechanicId: Long): MechanicLocation? {
        val response = mechanicService.getMechanicLocation(mechanicId)
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
