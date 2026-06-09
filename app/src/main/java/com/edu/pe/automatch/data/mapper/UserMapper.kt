package com.edu.pe.automatch.data.mapper

import com.edu.pe.automatch.data.local.entities.UserEntity
import com.edu.pe.automatch.data.remote.dtos.SignUpResponseDto
import com.edu.pe.automatch.data.remote.dtos.UserDto
import com.edu.pe.automatch.domain.model.User

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}

fun UserDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}

fun SignUpResponseDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}

fun SignUpResponseDto.toDomain(): User {
    return User(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        email = email,
        fullName = fullName,
        profilePicture = profilePicture,
        role = role
    )
}