package com.example.ajiekc.karoon.repository

import android.content.SharedPreferences
import com.example.ajiekc.karoon.extensions.get
import com.example.ajiekc.karoon.extensions.set
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val preferences: SharedPreferences
) {
    fun isAuthorized(): Boolean {
        return preferences[AUTH_PREF, false]
    }

    fun setAuthorized(flag: Boolean) {
        preferences[AUTH_PREF] = flag
    }

    fun getGoogleAcessToken(): String {
        return preferences[GOOGLE_ACCESS_TOKEN_PREF, ""]
    }

    fun setGoogleAccessToken(token: String?) {
        preferences[GOOGLE_ACCESS_TOKEN_PREF] = token
    }

    companion object {
        const val AUTH_PREF = "auth_pref"
        const val GOOGLE_ACCESS_TOKEN_PREF = "google_access_token_pref"
    }
}