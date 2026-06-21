package com.edu.pe.automatch.data.repository

import android.util.Log
import com.edu.pe.automatch.data.mapper.toDomain
import com.edu.pe.automatch.data.mapper.toUpdateDto
import com.edu.pe.automatch.data.remote.dtos.CreateSpecialtyDto
import com.edu.pe.automatch.data.remote.dtos.UpsertMechanicLocationDto
import com.edu.pe.automatch.data.remote.services.MechanicService
import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.Specialty
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
        val response = mechanicService.updateMechanic(mechanic.id, mechanic.toUpdateDto())
        Log.d(
            "MECHANIC_DEBUG",
            "updateMechanic(id=${mechanic.id}) body=${mechanic.toUpdateDto()} -> " +
                    "code=${response.code()}, error=${response.errorBody()?.string()}"
        )
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

    override suspend fun upsertMechanicLocation(
        mechanicId: Long,
        latitude: Double,
        longitude: Double,
        addressText: String?
    ): MechanicLocation? {
        val body = UpsertMechanicLocationDto(latitude, longitude, addressText)
        val response = mechanicService.upsertMechanicLocation(mechanicId, body)
        Log.d(
            "MECHANIC_DEBUG",
            "upsertLocation(mechanicId=$mechanicId) body=$body -> " +
                    "code=${response.code()}, error=${response.errorBody()?.string()}"
        )
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun getAllSpecialties(): List<Specialty> {
        val response = mechanicService.getAllSpecialties()
        return if (response.isSuccessful) {
            response.body()?.map { it.toDomain() } ?: emptyList()
        } else emptyList()
    }

    override suspend fun createSpecialty(name: String): Specialty? {
        val response = mechanicService.createSpecialty(CreateSpecialtyDto(name))
        Log.d("MECHANIC_DEBUG", "createSpecialty('$name') -> code=${response.code()}, body=${response.body()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun addSpecialtyToMechanic(mechanicProfileId: Long, specialtyId: Long): MechanicProfile? {
        val response = mechanicService.addSpecialtyToMechanic(mechanicProfileId, specialtyId)
        Log.d("MECHANIC_DEBUG", "addSpecialty(m=$mechanicProfileId, s=$specialtyId) -> code=${response.code()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }

    override suspend fun removeSpecialtyFromMechanic(mechanicProfileId: Long, specialtyId: Long): MechanicProfile? {
        val response = mechanicService.removeSpecialtyFromMechanic(mechanicProfileId, specialtyId)
        Log.d("MECHANIC_DEBUG", "removeSpecialty(m=$mechanicProfileId, s=$specialtyId) -> code=${response.code()}")
        return if (response.isSuccessful) response.body()?.toDomain() else null
    }
}
