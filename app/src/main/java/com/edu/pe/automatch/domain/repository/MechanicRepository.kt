package com.edu.pe.automatch.domain.repository

import com.edu.pe.automatch.domain.model.MechanicLocation
import com.edu.pe.automatch.domain.model.MechanicProfile

interface MechanicRepository {

    suspend fun getAllMechanics(): List<MechanicProfile>

    suspend fun getMechanicByUserId(userId: Long): MechanicProfile?

    suspend fun getAllLocations(): List<MechanicLocation>
}
