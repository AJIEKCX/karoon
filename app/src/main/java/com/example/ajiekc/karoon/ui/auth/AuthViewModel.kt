package com.example.ajiekc.karoon.ui.auth

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.ajiekc.karoon.Screens
import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val router: Router
) : BaseViewModel() {

    val viewState: MutableLiveData<AuthViewState> = MutableLiveData()

    fun getUserData(
        authType: AuthType,
        accessToken: String? = null,
        userId: String? = null,
        account: GoogleSignInAccount? = null
    ) {
        repository.getAuthData(authType, accessToken, userId, account, null)
            .doOnSubscribe { viewState.postValue(AuthViewState.loading()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { data ->
                    viewState.value = AuthViewState.content(data)
                    Log.i(AuthFragment::class.java.simpleName, "Success: ${data.firstName}")
                },
                onError = { error ->
                    viewState.value = AuthViewState.error(error)
                    Log.e(AuthFragment::class.java.simpleName, "Error: ${error.message}")
                }).disposeLater()
    }

    fun navigateToNewsfeed() {
        router.newRootScreen(Screens.News)
    }
}