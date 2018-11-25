package com.example.ajiekc.karoon.di.provider

import com.example.ajiekc.karoon.BuildConfig
import com.example.ajiekc.karoon.api.fb.FBService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class FBServiceApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<FBService> {
    override fun get(): FBService {
        return retrofit.newBuilder()
            .baseUrl(BuildConfig.FB_ENDPOINT)
            .build().run {
                create(FBService::class.java)
            }
    }
}