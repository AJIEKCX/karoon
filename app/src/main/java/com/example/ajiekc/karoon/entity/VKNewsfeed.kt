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
    val reposts: Int,
    val nextFrom: String?
) {
    constructor() : this(0, "", 0L, "", "", "", 0, 0, 0, null)

    var type: String? = null
}
