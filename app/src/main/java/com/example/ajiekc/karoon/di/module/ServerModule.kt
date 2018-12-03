package com.example.ajiekc.karoon.di.module

import com.example.ajiekc.karoon.api.fb.FBService
import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.api.youtube.YoutubeService
import com.example.ajiekc.karoon.di.provider.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import toothpick.config.Module

class ServerModule : Module() {
    init {
        bind(Gson::class.java).toInstance(Gson())

        bind(OkHttpClient::class.java)
            .toProvider(HttpClientProvider::class.java)
            .providesSingletonInScope()

        bind(Retrofit::class.java)
            .toProvider(RetrofitProvider::class.java)
            .providesSingletonInScope()

        bind(VKService::class.java)
            .toProvider(VKServiceApiProvider::class.java)
            .providesSingletonInScope()

        bind(FBService::class.java)
            .toProvider(FBServiceApiProvider::class.java)
            .providesSingletonInScope()

        bind(YoutubeService::class.java)
            .toProvider(YoutubeServiceApiProvider::class.java)
            .providesSingletonInScope()
    }
}