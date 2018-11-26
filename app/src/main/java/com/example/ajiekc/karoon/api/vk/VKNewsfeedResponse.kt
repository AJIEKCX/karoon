package com.example.ajiekc.karoon.api.vk

import com.google.gson.annotations.SerializedName

data class VKNewsfeedResponse(val response: Response) {
    data class Response(
        val items: List<Item>,
        val profiles: List<Profile>?,
        val groups: List<Group>?
    ) {
        data class Item(
            @SerializedName("post_id") val postId: Int,
            @SerializedName("source_id") val sourceId: Int,
            @SerializedName("text") val text: String?,
            @SerializedName("date") val date: Long?,
            @SerializedName("attachments") val attachments: List<Attachments>?,
            @SerializedName("likes") val likes: Like?,
            @SerializedName("reposts") val reposts: Repost?,
            @SerializedName("comments") val comments: Comment?
        ) {
            data class Like(
                val count: Int?
            )

            data class Repost(
                val count: Int?
            )

            data class Comment(
                val count: Int?
            )

            data class Attachments(
                val type: String?,
                val photo: Photo?
            ) {
                data class Photo(@SerializedName("photo_807") val photoUrl: String?)
            }
        }

        data class Profile(
            @SerializedName("id") val id: Int,
            @SerializedName("first_name") val firstName: String?,
            @SerializedName("last_name") val lastName: String?,
            @SerializedName("photo_100") val photoUrl: String?
        )

        data class Group(
            @SerializedName("id") val id: Int,
            @SerializedName("name") val name: String?,
            @SerializedName("photo_100") val photoUrl: String?
        )
    }
}