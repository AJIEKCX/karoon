package com.example.ajiekc.karoon.api.youtube

import com.google.gson.annotations.SerializedName

data class YoutubeActivitiesResponse(val response: Response) {
    data class Response(
            val kind: String?,
            val etag: String?,
            val nextPageToken: String?,
            val pageInfo: PageInfo?,
            val items: List<Item>?
    ) {
        data class PageInfo(
                val totalResults: Int?,
                val resultsPerPage: Int?
        )

        data class Item(
                @SerializedName("kind") val kind: String?,
                @SerializedName("etag") val etag: String?,
                @SerializedName("id") val text: String?,
                @SerializedName("snippet") val date: Snippet?,
                @SerializedName("contentDetails") val contentDetails: ContentDetails?
        ) {

            data class Snippet(
                    val publishedAt: String?,
                    val title: String?,
                    val resourceId: Resource_id?,
                    val channelId: String?,
                    val thumbnails: Thumbnails?
            ) {
                data class Resource_id(
                        val channelId: String?
                )

                data class Thumbnails(
                        val default: URL?,
                        val medium: URL?,
                        val high: URL?,
                        val standard: URL?,
                        val maxres: URL?
                ) {
                    data class URL(
                            val url: String?,
                            val width: Int?,
                            val height: Int?
                    )
                }
            }

            data class ContentDetails(
                    val like: Like?
            ) {
                data class Like(
                        val resourceId: Resource_id?
                ) {
                    data class Resource_id(
                            val videoId: String?
                    )
                }
            }
        }
    }
}
