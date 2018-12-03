package com.example.ajiekc.karoon.ui.profile

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.example.ajiekc.karoon.Screens
import com.example.ajiekc.karoon.entity.AuthData
import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.ui.auth.AuthType
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import com.example.ajiekc.karoon.ui.common.SingleLiveEvent
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val router: Router
) : BaseViewModel() {

    init {
        getLastAuthData()
    }

    val isLoading = ObservableBoolean()
    val userName = ObservableField<String>()
    val photoUrl = ObservableField<String>()
    val authType = ObservableField<AuthType>()

    val loginClickEvent = SingleLiveEvent<String>()
    val logoutClickEvent = SingleLiveEvent<String>()

    fun getUserData(
        authType: AuthType,
        accessToken: String? = null,
        userId: String? = null,
        account: GoogleSignInAccount? = null,
        authCode: String? = null
    ) {
        repository.getAuthData(authType, accessToken, userId, account, authCode)
            .doOnSubscribe { isLoading.set(true) }
            .doAfterTerminate { isLoading.set(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    showInfo(data)
                    Timber.d("Success")
                },
                onError = { error ->
                    Timber.e(error)
                }).disposeLater()
    }

    fun getLastAuthData() {
        repository.getLastAuthData()
            .doOnSubscribe { isLoading.set(true) }
            .doAfterTerminate { isLoading.set(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    showInfo(data)
                    Timber.d("Success")
                },
                onError = { error ->
                    Timber.e(error)
                },
                onComplete = {
                    logout()
                }).disposeLater()
    }

    fun changeProfile(type: AuthType) {
        repository.getAuthData(type)
            .doOnSubscribe { isLoading.set(true) }
            .doAfterTerminate { isLoading.set(false) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    showInfo(data)
                    Timber.d("Success")
                },
                onError = { error ->
                    Timber.e(error)
                },
                onComplete = {
                    loginClickEvent.value = type.name
                }).disposeLater()
    }

    fun logout(type: AuthType) {
        repository.logout(type)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onComplete = {
                    logoutClickEvent.value = type.name
                    getLastAuthData()
                },
                onError = { error ->
                    Timber.e(error)
                }).disposeLater()
    }

    private fun logout() {
        repository.setAuthorized(false)
        router.newRootScreen(Screens.Auth)
    }

    private fun showInfo(data: AuthData?) {
        userName.set("${data?.firstName ?: ""} ${data?.lastName ?: ""}")
        photoUrl.set(data?.photoUrl)
        authType.set(AuthType.fromValue(data?.authType))
    }
}