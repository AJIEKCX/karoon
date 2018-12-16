package com.example.ajiekc.karoon.mapper

import com.example.ajiekc.karoon.api.youtube.YoutubeVideosResponse
import com.example.ajiekc.karoon.entity.YoutubeVideo

object YoutubeVideoMapper {
    fun map(resp: YoutubeVideosResponse, channelPhotoUrl: String?): List<YoutubeVideo> {
        return resp.items!!.map {
            YoutubeVideo(
                text = it.snippet?.title ?: "",
                date = it.snippet?.publishedAt?.time ?: 0L,
                videoPreviewUrl = it.snippet?.thumbnails?.high?.url ?: "",
                channelName = it.snippet?.channelTitle ?: "",
                channelPhotoUrl = channelPhotoUrl ?: "",
                videoId = it.id?.videoId ?: ""
            )
        }
    }
}