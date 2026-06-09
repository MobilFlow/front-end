package com.edu.pe.automatch.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,

    val email: String,

    val fullName: String,

    val profilePicture: String?,

    val role: String
)