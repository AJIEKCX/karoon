package com.example.ajiekc.karoon.api.youtube

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface YoutubeService {
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("https://www.googleapis.com/oauth2/v4/token")
    fun getAccessToken(
        @Query("grant_type") grantType: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("code") authCode: String?
    ): Single<TokenResponse>

    @GET("subscriptions")
    fun getSubscriptions(
        @Query("part") part: String,
        @Query("mine") mine: String,
        @Query("maxResults") maxResults: Int
    ): Single<YoutubeSubscriptionsResponse>

    @GET("search")
    fun getLastVideos(
        @Query("part") part: String,
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int,
        @Query("order") order: String,
        @Query("type") type: String
    ): Single<YoutubeVideosResponse>
}