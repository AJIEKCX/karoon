package com.example.ajiekc.karoon.ui.main

import com.example.ajiekc.karoon.Screens
import com.example.ajiekc.karoon.repository.SessionRepository
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val router: Router,
    private val sessionRepository: SessionRepository
) : BaseViewModel() {

    init {
        initNavigation()
    }

    private fun initNavigation() {
        if (sessionRepository.isAuthorized()) {
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