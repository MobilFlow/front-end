    package com.edu.pe.automatch.di
    import com.edu.pe.automatch.data.remote.interceptor.AuthInterceptor
    import com.edu.pe.automatch.data.remote.services.AuthService
    import com.edu.pe.automatch.data.remote.services.DriverService
    import com.edu.pe.automatch.data.remote.services.MechanicService
    import com.edu.pe.automatch.data.remote.services.ReputationService
    import com.edu.pe.automatch.data.remote.services.ServiceCatalogService
    import com.edu.pe.automatch.data.remote.services.ServiceRequestService
    import com.edu.pe.automatch.data.remote.services.UserService
    import okhttp3.OkHttpClient
    import retrofit2.Retrofit
    import retrofit2.converter.gson.GsonConverterFactory
    import okhttp3.logging.HttpLoggingInterceptor

    object RemoteModule {

        fun provideBaseUrl(): String {
            return "https://back-end-automatch1.onrender.com/api/v1/"
        }

        fun provideRetrofit(
            baseUrl: String = provideBaseUrl()
        ): Retrofit {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(AuthInterceptor())
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        fun provideUserService(
            retrofit: Retrofit = provideRetrofit()
        ): UserService = retrofit.create(UserService::class.java)

        fun provideAuthService(
            retrofit: Retrofit = provideRetrofit()
        ): AuthService = retrofit.create(AuthService::class.java)

        fun provideMechanicService(
            retrofit: Retrofit = provideRetrofit()
        ): MechanicService = retrofit.create(MechanicService::class.java)

        fun provideDriverService(
            retrofit: Retrofit = provideRetrofit()
        ): DriverService = retrofit.create(DriverService::class.java)

        fun provideServiceRequestService(
            retrofit: Retrofit = provideRetrofit()
        ): ServiceRequestService = retrofit.create(ServiceRequestService::class.java)

        fun provideReputationService(
            retrofit: Retrofit = provideRetrofit()
        ): ReputationService = retrofit.create(ReputationService::class.java)

        fun provideServiceCatalogService(
            retrofit: Retrofit = provideRetrofit()
        ): ServiceCatalogService = retrofit.create(ServiceCatalogService::class.java)
    }
