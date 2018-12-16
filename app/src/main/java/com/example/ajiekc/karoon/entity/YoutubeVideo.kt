package com.example.ajiekc.karoon.entity

data class YoutubeVideo(
    val text: String,
    val date: Long,
    val videoPreviewUrl: String,
    val channelName: String,
    val channelPhotoUrl: String,
    val videoId: String
)
