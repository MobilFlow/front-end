    package com.edu.pe.automatch.di
    import com.edu.pe.automatch.data.remote.interceptor.AuthInterceptor
    import com.edu.pe.automatch.data.remote.services.AuthService
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
        ): UserService {

            return retrofit.create(UserService::class.java)
        }

        fun provideAuthService(
            retrofit: Retrofit = provideRetrofit()
        ): AuthService {

            return retrofit.create(AuthService::class.java)
        }
    }