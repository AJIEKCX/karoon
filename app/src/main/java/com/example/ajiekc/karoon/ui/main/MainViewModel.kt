package com.example.ajiekc.karoon.ui.main

import android.util.Log
import com.example.ajiekc.karoon.Screens
import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import ru.terrakok.cicerone.Router
import timber.log.Timber
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    init {
        Log.i("MainViewModel", "asdsadasd")
        initNavigation()
    }

    private fun initNavigation() {
        Timber.d("is authorize = ${authRepository.isAuthorized()}")
        if (authRepository.isAuthorized()) {
            router.newRootScreen(Screens.News)
        } else {
            router.newRootScreen(Screens.Auth)
        }
    }

    fun navigateToNewsfeed() {
        router.newScreenChain(Screens.News)
    }

    fun navigateToProfile() {
        router.newScreenChain(Screens.Profile)
    }
}