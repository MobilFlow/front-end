package com.edu.pe.automatch.di


import com.edu.pe.automatch.data.local.SessionManager
import com.edu.pe.automatch.data.local.dao.UserDao
import com.edu.pe.automatch.data.remote.services.AuthService
import com.edu.pe.automatch.data.remote.services.UserService
import com.edu.pe.automatch.data.repository.UserRepositoryImpl
import com.edu.pe.automatch.domain.repository.UserRepository

object RepositoryModule {

    fun provideUserRepository(
        userService: UserService = RemoteModule.provideUserService(),
        authService: AuthService = RemoteModule.provideAuthService(),
        userDao: UserDao = LocalModule.provideUserDao()

    ): UserRepository {

        return UserRepositoryImpl(
            userService,
            authService,
            userDao,
            SessionManager
        )
    }

    }
