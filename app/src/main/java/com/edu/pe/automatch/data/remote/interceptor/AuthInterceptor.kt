package com.edu.pe.automatch.data.remote.interceptor

import android.util.Log
import com.edu.pe.automatch.data.local.SessionManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        Log.d("TOKEN", "Interceptor token = ${SessionManager.token}")

        val requestBuilder = chain.request()
            .newBuilder()

        SessionManager.token?.let {
            requestBuilder.addHeader(
                "Authorization",
                "Bearer $it"
            )
        }

        return chain.proceed(requestBuilder.build())
    }
}