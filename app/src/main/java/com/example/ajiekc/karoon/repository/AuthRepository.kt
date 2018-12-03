package com.example.ajiekc.karoon.repository

import android.content.SharedPreferences
import com.example.ajiekc.karoon.api.fb.FBService
import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.api.youtube.YoutubeService
import com.example.ajiekc.karoon.db.AuthDao
import com.example.ajiekc.karoon.entity.AuthData
import com.example.ajiekc.karoon.extensions.PreferenceHelper
import com.example.ajiekc.karoon.extensions.get
import com.example.ajiekc.karoon.extensions.set
import com.example.ajiekc.karoon.ui.auth.AuthType
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val vkService: VKService,
    private val fbService: FBService,
    private val youtubeService: YoutubeService,
    private val preferences: SharedPreferences,
    private val authDao: AuthDao
) {
    fun getAuthData(
        authType: AuthType, accessToken: String?, userId: String?,
        account: GoogleSignInAccount?, authCode: String?
    ): Single<AuthData> {
        return when (authType) {
            AuthType.VK -> getVkData(accessToken, userId)
            AuthType.FB -> getFbData(accessToken)
            AuthType.GOOGLE -> getGoogleData(account, authCode)
        }
            .doOnSuccess { authDao.insert(it) }
    }

    private fun getVkData(accessToken: String?, userId: String?): Single<AuthData> =
        vkService.getUser(userId ?: "", "photo_max_orig", "5.52", accessToken ?: "")
            .map { resp -> resp.response?.first() }
            .map { resp -> AuthData(AuthType.VK.name, resp.firstName, resp.lastName, resp.photoMaxOrig, accessToken) }

    private fun getFbData(accessToken: String?): Single<AuthData> =
        fbService.me("picture.width(640),first_name,last_name", accessToken ?: "")
            .map { resp -> AuthData(AuthType.FB.name, resp.firstName, resp.lastName, resp.picture?.data?.url, accessToken) }

    private fun getGoogleData(account: GoogleSignInAccount?, authCode: String?): Single<AuthData> =
        youtubeService.getAccessToken("authorization_code", "294611782255-a8rfjhnm0l2naa4b9qia69ig31okf0lv.apps.googleusercontent.com", "8JEQZaXXYiH2It069sr4JQsz", authCode)
            .map {
                AuthData(AuthType.GOOGLE.name, account?.givenName, account?.familyName, account?.photoUrl.toString(), it.accessToken)
            }

    fun getAuthData(type: AuthType): Maybe<AuthData> {
        return authDao.get(type.name)
    }

    fun getLastAuthData(): Maybe<AuthData> {
        return authDao.getAll()
            .toObservable()
            .flatMap {
                Observable.fromIterable(it)
            }.lastElement()
    }

    fun logout(type: AuthType): Completable {
        return Completable.fromCallable {
            authDao.delete(type.name)
        }
    }

    fun isAuthorized(): Boolean {
        return preferences[PreferenceHelper.AUTH_PREF, false]
    }

    fun setAuthorized(flag: Boolean) {
        preferences[PreferenceHelper.AUTH_PREF] = flag
    }
}