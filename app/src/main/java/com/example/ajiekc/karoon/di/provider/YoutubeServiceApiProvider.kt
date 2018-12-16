package com.example.ajiekc.karoon.di.provider

import com.example.ajiekc.karoon.BuildConfig
import com.example.ajiekc.karoon.api.youtube.YoutubeService
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Provider

class YoutubeServiceApiProvider @Inject constructor(
    private val retrofit: Retrofit
) : Provider<YoutubeService> {
    override fun get(): YoutubeService {
        return retrofit.newBuilder()
            .baseUrl(BuildConfig.YOUTUBE_ENDPOINT)
            .build().run {
                create(YoutubeService::class.java)
            }
    }
}