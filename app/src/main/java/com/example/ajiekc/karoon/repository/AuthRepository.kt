package com.example.ajiekc.karoon.repository

import com.example.ajiekc.karoon.api.fb.FBService
import com.example.ajiekc.karoon.api.vk.VKService
import com.example.ajiekc.karoon.entity.AuthData
import com.example.ajiekc.karoon.ui.auth.AuthType
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.Single
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val vkService: VKService,
    private val fbService: FBService
) {
    fun getUserData(
        authType: AuthType, accessToken: String?, userId: String?,
        account: GoogleSignInAccount?
    ): Single<AuthData> =
        when (authType) {
            AuthType.VK -> getVkData(accessToken!!, userId!!)
            AuthType.FB -> getFbData(accessToken!!)
            AuthType.GOOGLE -> getGoogleData(account!!)
        }

    private fun getVkData(accessToken: String, userId: String): Single<AuthData> =
        vkService.getUser(userId, "photo_max_orig", "5.52", accessToken)
            .map { resp -> resp.response?.first() }
            .map { resp -> AuthData(resp.firstName, resp.lastName, resp.photoMaxOrig) }

    private fun getFbData(accessToken: String): Single<AuthData> =
        fbService.me("picture.width(640),first_name,last_name", accessToken)
            .map { resp -> AuthData(resp.firstName, resp.lastName, resp.picture?.data?.url) }

    private fun getGoogleData(account: GoogleSignInAccount): Single<AuthData> = Single.just(
        AuthData(account.givenName, account.familyName, account.photoUrl.toString())
    )
}