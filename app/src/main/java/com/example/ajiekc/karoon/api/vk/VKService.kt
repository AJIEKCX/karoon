package com.example.ajiekc.karoon.api.vk

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VKService {

    @GET("newsfeed.get")
    fun getNews(
        @Query("filters") filters: String,
        @Query("v") version: String,
        @Query("access_token") accessToken: String
    ): Single<VKNewsfeedResponse>

    @GET("users.get")
    fun getUser(
        @Query("user_id") userId: String,
        @Query("fields") fields: String,
        @Query("v") version: String,
        @Query("access_token") accessToken: String
    ): Single<VKUserResponse>
}