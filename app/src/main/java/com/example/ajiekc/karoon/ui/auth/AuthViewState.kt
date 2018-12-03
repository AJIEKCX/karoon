package com.example.ajiekc.karoon.ui.auth

import com.example.ajiekc.karoon.entity.AuthData

class AuthViewState(val state: LceAuthState, val data: AuthData? = null, val error: Throwable? = null) {
    companion object {
        fun content(data: AuthData?) = AuthViewState(LceAuthState.CONTENT, data)
        fun loading() = AuthViewState(LceAuthState.LOADING)
        fun error(error: Throwable?) = AuthViewState(LceAuthState.ERROR, error = error)
    }
}