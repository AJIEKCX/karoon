package com.example.ajiekc.karoon.ui.auth

import com.example.ajiekc.karoon.LceState
import com.example.ajiekc.karoon.data.AuthData

class AuthViewState(val state: LceState, val data: AuthData? = null, val error: Throwable? = null) {
    companion object {
        fun content(data: AuthData?) = AuthViewState(LceState.CONTENT, data)
        fun loading() = AuthViewState(LceState.LOADING)
        fun error(error: Throwable?) = AuthViewState(LceState.ERROR, error = error)
    }
}