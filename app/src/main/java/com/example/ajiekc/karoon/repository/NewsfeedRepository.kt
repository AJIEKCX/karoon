package com.example.ajiekc.karoon.repository

import android.content.SharedPreferences
import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.entity.VKNewsfeed
import com.example.ajiekc.karoon.extensions.get
import com.example.ajiekc.karoon.mapper.VKNewsfeedMapper
import com.example.ajiekc.karoon.ui.auth.AuthActivity
import io.reactivex.Single
import javax.inject.Inject

class NewsfeedRepository @Inject constructor(
    private val vkService: VKService,
    private val preferences: SharedPreferences
) {
    fun loadNews(): Single<List<VKNewsfeed>> {
        val accessToken = preferences[AuthActivity.AUTH_TOKEN, ""] ?: ""
        return vkService.getNews("post", "5.52", accessToken)
            .map { it.response }
            .map { VKNewsfeedMapper.map(it) }
    }
}
