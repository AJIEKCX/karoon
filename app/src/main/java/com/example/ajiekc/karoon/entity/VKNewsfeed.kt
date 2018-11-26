package com.example.ajiekc.karoon.entity

data class VKNewsfeed(
    val postId: Int,
    val text: String,
    val date: Long,
    val photoUrl: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val likes: Int,
    val comments: Int,
    val reposts: Int
)
