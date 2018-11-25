package com.example.ajiekc.karoon.di.provider

import com.example.ajiekc.karoon.BuildConfig
import com.example.ajiekc.karoon.api.vk.VKService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class VKServiceApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<VKService> {
    override fun get(): VKService {
        return retrofit.newBuilder()
            .baseUrl(BuildConfig.VK_ENDPOINT)
            .build().run {
                create(VKService::class.java)
            }
    }
}