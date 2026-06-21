package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile
import com.edu.pe.automatch.domain.model.Specialty

interface MechanicRepository {

    suspend fun getAllMechanics(): List<MechanicProfile>

    suspend fun getMechanicByUserId(userId: Long): MechanicProfile?

    suspend fun updateMechanic(mechanic: MechanicProfile): Boolean

    suspend fun getAllLocations(): List<MechanicLocation>

    suspend fun getMechanicLocation(mechanicId: Long): MechanicLocation?

    suspend fun upsertMechanicLocation(
        mechanicId: Long,
        latitude: Double,
        longitude: Double,
        addressText: String?
    ): MechanicLocation?

    suspend fun getAllSpecialties(): List<Specialty>

    suspend fun createSpecialty(name: String): Specialty?

    suspend fun addSpecialtyToMechanic(mechanicProfileId: Long, specialtyId: Long): MechanicProfile?

    suspend fun removeSpecialtyFromMechanic(mechanicProfileId: Long, specialtyId: Long): MechanicProfile?
}
