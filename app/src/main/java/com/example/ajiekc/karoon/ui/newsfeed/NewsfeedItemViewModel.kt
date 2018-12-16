package com.example.ajiekc.karoon.ui.newsfeed

import java.util.*

data class NewsfeedItemViewModel(
    val postId: Int,
    val text: String,
    val date: Date,
    val photoUrl: String,
    val authorName: String,
    val authorPhotoUrl: String,
    val likes: Int,
    val comments: Int,
    val reposts: Int,
    val nextFrom: String?,
    val socialType: String,
    val videoId: String
) {
    constructor() : this(0, "", Date(), "", "", "", 0, 0, 0, null, "", "")

    var type: String? = null
}
