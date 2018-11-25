package com.example.ajiekc.karoon.api.vk

import com.google.gson.annotations.SerializedName

data class VKNewsfeedResponse(@SerializedName("response") val response: Response?) {

    data class Response(@SerializedName("items") val items: List<Item>?)

    data class Item(
        @SerializedName("post_id") val postId: Int?,
        @SerializedName("text") val text: String?,
        @SerializedName("date") val date: Long?
    )
}