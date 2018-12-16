package com.example.ajiekc.karoon.api.youtube

data class YoutubeSubscriptionsResponse(
    val nextPageToken: String?,
    val pageInfo: PageInfo?,
    val items: List<Item>?
) {
    data class PageInfo(
        val totalResults: Int?,
        val resultsPerPage: Int?
    )

    data class Item(
        val snippet: Snippet?
    ) {
        data class Snippet(
            val title: String?,
            val resourceId: ResourceId?,
            val thumbnails: Thumbnails?
        ) {
            data class ResourceId(
                val channelId: String
            )

            data class Thumbnails(
                val default: URL?,
                val medium: URL?,
                val high: URL?
            ) {
                data class URL(
                    val url: String?
                )
            }
        }
    }
}