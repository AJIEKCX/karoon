package com.example.ajiekc.karoon.di.module

import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.api.youtube.AuthInterceptor
import com.example.ajiekc.karoon.api.youtube.YoutubeService
import com.example.ajiekc.karoon.di.provider.HttpClientProvider
import com.example.ajiekc.karoon.di.provider.RetrofitProvider
import com.example.ajiekc.karoon.di.provider.VKServiceApiProvider
import com.example.ajiekc.karoon.di.provider.YoutubeServiceApiProvider
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

        bind(YoutubeService::class.java)
            .toProvider(YoutubeServiceApiProvider::class.java)
            .providesSingletonInScope()

        bind(AuthInterceptor::class.java)
            .singletonInScope()
    }
}