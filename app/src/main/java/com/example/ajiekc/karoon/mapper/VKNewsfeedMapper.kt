package com.example.ajiekc.karoon.mapper

import com.example.ajiekc.karoon.api.vk.VKNewsfeedResponse
import com.example.ajiekc.karoon.entity.VKNewsfeed
import kotlin.math.abs

object VKNewsfeedMapper {
    fun map(resp: VKNewsfeedResponse.Response): List<VKNewsfeed> {
        return resp.items.map { newsfeed ->

            val authorName: String
            val authorPhotoUrl: String
            if (newsfeed.sourceId < 0) {
                val group = resp.groups?.find { it.id == abs(newsfeed.sourceId) }
                authorName = group?.name ?: ""
                authorPhotoUrl = group?.photoUrl ?: ""
            } else {
                val user = resp.profiles?.find { it.id == newsfeed.sourceId }
                authorName = "${user?.firstName ?: ""} ${user?.lastName ?: ""}"
                authorPhotoUrl = user?.photoUrl ?: ""
            }

            VKNewsfeed(
                postId = newsfeed.postId,
                text = newsfeed.text ?: "",
                date = newsfeed.date ?: 0,
                photoUrl = newsfeed.attachments?.find { it.type == "photo" }?.photo?.photoUrl ?: "",
                authorName = authorName,
                authorPhotoUrl = authorPhotoUrl,
                likes = newsfeed.likes?.count ?: 0,
                comments = newsfeed.comments?.count ?: 0,
                reposts = newsfeed.reposts?.count ?: 0
            )
        }
    }
}