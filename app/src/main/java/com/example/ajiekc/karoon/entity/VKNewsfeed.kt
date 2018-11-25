package com.example.ajiekc.karoon.entity

import com.google.gson.annotations.SerializedName

data class VKNewsfeed(
    @SerializedName("post_id") val postId: Int?,
    @SerializedName("text") val text: String?,
    @SerializedName("date") val date: Long?
)
