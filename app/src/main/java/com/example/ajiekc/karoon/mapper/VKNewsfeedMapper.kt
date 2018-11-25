package com.example.ajiekc.karoon.mapper

import com.example.ajiekc.karoon.api.vk.VKNewsfeedResponse
import com.example.ajiekc.karoon.entity.VKNewsfeed

object VKNewsfeedMapper {
    fun map(resp: VKNewsfeedResponse.Item): VKNewsfeed {
        return VKNewsfeed(
            postId = resp.postId ?: 0,
            text = resp.text ?: "",
            date = resp.date ?: 0
        )
    }
}