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

    @GET("/youtube/v3/subscriptions")
    fun getSubsriptions(
            @Query("part") part: String,
            @Query("mine") mine: String,
            @Query("key") key: String
    ): Single<YoutubeSubsriptionsResponse>

    @GET("/youtube/v3/activities")
    fun getActivities(
            @Query("part") part: String,
            @Query("mine") mine: String,
            @Query("maxResults") maxResults: String,
            @Query("key") key: String
    ): Single<YoutubeActivitiesResponse>

}