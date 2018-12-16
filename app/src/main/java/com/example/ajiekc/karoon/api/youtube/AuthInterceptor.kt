package com.example.ajiekc.karoon.api.youtube

import com.example.ajiekc.karoon.repository.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionRepository: SessionRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (!originalRequest.url().host().contains("googleapis")) {
            return chain.proceed(originalRequest)
        }
        val token = sessionRepository.getGoogleAcessToken()

        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}