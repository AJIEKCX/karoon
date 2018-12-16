package com.example.ajiekc.karoon.api.youtube

import java.util.*

data class YoutubeVideosResponse(
    val nextPageToken: String?,
    val pageInfo: PageInfo?,
    val items: List<Item>?
) {

    data class PageInfo(
        val totalResults: Int?,
        val resultsPerPage: Int?
    )

    data class Item(
        val snippet: Snippet?,
        val id: Id?
    ) {
        data class Snippet(
            val publishedAt: Date?,
            val title: String?,
            val channelId: String?,
            val thumbnails: Thumbnails?,
            val channelTitle: String?
        ) {
            data class Thumbnails(
                val default: URL?,
                val medium: URL?,
                val high: URL?
            ) {
                data class URL(
                    val url: String?,
                    val width: Int?,
                    val height: Int?
                )
            }
        }

        data class Id(
            val videoId: String?
        )
    }
}