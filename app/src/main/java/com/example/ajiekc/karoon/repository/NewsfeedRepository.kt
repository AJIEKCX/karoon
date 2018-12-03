package com.example.ajiekc.karoon.repository

import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.db.AuthDao
import com.example.ajiekc.karoon.entity.VKNewsfeed
import com.example.ajiekc.karoon.mapper.VKNewsfeedMapper
import com.example.ajiekc.karoon.ui.auth.AuthType
import io.reactivex.Single
import javax.inject.Inject

class NewsfeedRepository @Inject constructor(
    private val vkService: VKService,
    private val authDao: AuthDao
) {
    fun loadNews(startFrom: String? = null): Single<List<VKNewsfeed>> {
        return authDao.get(AuthType.VK.name)
            .toSingle()
            .flatMap {
                vkService.getNews("post", "5.52", it.accessToken ?: "", startFrom)
            }
            .map { it.response }
            .map { VKNewsfeedMapper.map(it) }
            .map { newsfeed -> newsfeed.filter { it.photoUrl.isNotEmpty() || it.text.isNotEmpty() } }
    }
}
