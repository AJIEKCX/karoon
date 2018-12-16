package com.example.ajiekc.karoon.repository

import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.api.youtube.YoutubeService
import com.example.ajiekc.karoon.db.AuthDao
import com.example.ajiekc.karoon.entity.VKNewsfeed
import com.example.ajiekc.karoon.entity.YoutubeVideo
import com.example.ajiekc.karoon.mapper.VKNewsfeedMapper
import com.example.ajiekc.karoon.mapper.YoutubeVideoMapper
import com.example.ajiekc.karoon.ui.auth.AuthType
import com.example.ajiekc.karoon.ui.newsfeed.NewsfeedItemViewModel
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsfeedRepository @Inject constructor(
    private val vkService: VKService,
    private val youtubeService: YoutubeService,
    private val sessionRepository: SessionRepository,
    private val authDao: AuthDao
) {
    fun loadNews(startFrom: String? = null): Single<List<NewsfeedItemViewModel>> {
        val youtubeSource: Observable<YoutubeVideo> =
            if (sessionRepository.getGoogleAcessToken().isNotEmpty()) {
                youtubeService.getSubscriptions("snippet", "true", 50)
                    .toObservable()
                    .flatMap { Observable.fromIterable(it.items) }
                    .map { it.snippet?.resourceId?.channelId to it.snippet?.thumbnails?.high?.url }
                    .flatMapSingle { (channelId, channelPhotoUrl) ->
                        youtubeService.getLastVideos("snippet", channelId!!, 10, "date", "video")
                            .map { YoutubeVideoMapper.map(it, channelPhotoUrl) }
                    }
                    .flatMap { Observable.fromIterable(it) }
                    .filter { it.date + TimeUnit.DAYS.toMillis(7) - Date().time > 0 }
            } else {
                Observable.empty()
            }

        val vkSource: Observable<NewsfeedItemViewModel> = authDao.get(AuthType.VK.name)
            .flatMapSingle {
                vkService.getNews("post", "5.52", it.accessToken ?: "", startFrom)
            }
            .map { VKNewsfeedMapper.map(it.response) }
            .map { newsfeed -> newsfeed.filter { it.photoUrl.isNotEmpty() || it.text.isNotEmpty() } }
            .flatMapObservable { Observable.fromIterable(it) }
            .map {
                createViewModel(it)
            }

        val youtubeSourceFiltered: Observable<NewsfeedItemViewModel> = vkSource.sorted { o1, o2 -> o2.date.compareTo(o1.date) }
            .lastOrError()
            .toObservable()
            .flatMap { lastVKItem ->
                youtubeSource.filter {
                    Date(it.date).after(lastVKItem.date)
                }
            }.map {
                createViewModel(it)
            }

        return Observable.merge(vkSource, youtubeSourceFiltered)
            .sorted { o1, o2 -> o2.date.compareTo(o1.date) }
            .toList()
    }

    private fun createViewModel(video: YoutubeVideo): NewsfeedItemViewModel {
        return NewsfeedItemViewModel(
            postId = 0,
            text = video.text,
            date = Date(video.date),
            photoUrl = video.videoPreviewUrl,
            authorName = video.channelName,
            authorPhotoUrl = video.channelPhotoUrl,
            likes = 0,
            comments = 0,
            reposts = 0,
            nextFrom = "",
            socialType = AuthType.GOOGLE.name,
            videoId = video.videoId
        )
    }

    private fun createViewModel(newsfeed: VKNewsfeed): NewsfeedItemViewModel {
        return NewsfeedItemViewModel(
            postId = newsfeed.postId,
            text = newsfeed.text,
            date = Date(newsfeed.date),
            photoUrl = newsfeed.photoUrl,
            authorName = newsfeed.authorName,
            authorPhotoUrl = newsfeed.authorPhotoUrl,
            likes = newsfeed.likes,
            comments = newsfeed.comments,
            reposts = newsfeed.reposts,
            nextFrom = newsfeed.nextFrom,
            socialType = AuthType.VK.name,
            videoId = ""
        )
    }
}