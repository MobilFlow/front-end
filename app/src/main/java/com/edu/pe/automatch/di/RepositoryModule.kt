package com.edu.pe.automatch.di

import com.edu.pe.automatch.data.local.SessionManager
import com.edu.pe.automatch.data.local.dao.UserDao
import com.edu.pe.automatch.data.repository.DriverRepositoryImpl
import com.edu.pe.automatch.data.repository.MechanicRepositoryImpl
import com.edu.pe.automatch.data.repository.ReviewRepositoryImpl
import com.edu.pe.automatch.data.repository.ServiceCatalogRepositoryImpl
import com.edu.pe.automatch.data.repository.ServiceRequestRepositoryImpl
import com.edu.pe.automatch.data.repository.UserRepositoryImpl
import com.edu.pe.automatch.data.remote.services.AuthService
import com.edu.pe.automatch.data.remote.services.DriverService
import com.edu.pe.automatch.data.remote.services.MechanicService
import com.edu.pe.automatch.data.remote.services.ReputationService
import com.edu.pe.automatch.data.remote.services.ServiceCatalogService
import com.edu.pe.automatch.data.remote.services.ServiceRequestService
import com.edu.pe.automatch.data.remote.services.UserService
import com.edu.pe.automatch.domain.repository.DriverRepository
import com.edu.pe.automatch.domain.repository.MechanicRepository
import com.edu.pe.automatch.domain.repository.ReviewRepository
import com.edu.pe.automatch.domain.repository.ServiceCatalogRepository
import com.edu.pe.automatch.domain.repository.ServiceRequestRepository
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

    fun provideMechanicRepository(
        mechanicService: MechanicService = RemoteModule.provideMechanicService()
    ): MechanicRepository {
        return MechanicRepositoryImpl(mechanicService)
    }

    fun provideDriverRepository(
        driverService: DriverService = RemoteModule.provideDriverService()
    ): DriverRepository {
        return DriverRepositoryImpl(driverService)
    }

    fun provideServiceRequestRepository(
        serviceRequestService: ServiceRequestService = RemoteModule.provideServiceRequestService()
    ): ServiceRequestRepository {
        return ServiceRequestRepositoryImpl(serviceRequestService)
    }

    fun provideReviewRepository(
        reputationService: ReputationService = RemoteModule.provideReputationService()
    ): ReviewRepository {
        return ReviewRepositoryImpl(reputationService)
    }

    fun provideServiceCatalogRepository(
        serviceCatalogService: ServiceCatalogService = RemoteModule.provideServiceCatalogService()
    ): ServiceCatalogRepository {
        return ServiceCatalogRepositoryImpl(serviceCatalogService)
    }
}
