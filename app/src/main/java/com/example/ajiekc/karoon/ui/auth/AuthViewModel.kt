package com.example.ajiekc.karoon.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : BaseViewModel() {

    val viewState: MutableLiveData<AuthViewState> = MutableLiveData()

    fun getUserData(
        authType: AuthType,
        accessToken: String? = null,
        userId: String? = null,
        account: GoogleSignInAccount? = null
    ) {
        repository.getUserData(authType, accessToken, userId, account)
            .doOnSubscribe { viewState.postValue(AuthViewState.loading()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    viewState.value = AuthViewState.content(data)
                    Log.i(AuthActivity::class.java.simpleName, "Success: ${data.firstName}")
                },
                onError = { error ->
                    viewState.value = AuthViewState.error(error)
                    Log.e(AuthActivity::class.java.simpleName, "Error: ${error.message}")
                }).disposeLater()
    }
}