package com.example.ajiekc.karoon.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.Screens
import com.example.ajiekc.karoon.extensions.toast
import com.example.ajiekc.karoon.ui.auth.AuthFragment
import com.example.ajiekc.karoon.ui.newsfeed.NewsfeedFragment
import com.example.ajiekc.karoon.ui.profile.ProfileFragment
import ru.terrakok.cicerone.android.SupportAppNavigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Forward
import ru.terrakok.cicerone.commands.Replace
import timber.log.Timber

class MainNavigator constructor(private val activity: FragmentActivity) : SupportAppNavigator(activity, R.id.content) {

    override fun createActivityIntent(context: Context, screenKey: String?, data: Any?): Intent? {
        return when (screenKey) {
            Screens.ExternalUrl -> createUrlNavigationIntent(data as String)
            else -> null
        }
    }

    override fun createFragment(screenKey: String?, data: Any?): Fragment? {
        return when (screenKey) {
            Screens.Auth -> AuthFragment()
            Screens.Profile -> ProfileFragment()
            Screens.News -> NewsfeedFragment()
            else -> null
        }
    }

    override fun showSystemMessage(message: String) {
        activity.toast(message)
    }

    override fun unknownScreen(command: Command?) {
        val screenKey = when (command) {
            is Forward -> command.screenKey
            is Replace -> command.screenKey
            is BackTo -> command.screenKey
            else -> null
        }

        if (!screenKey.isNullOrEmpty()) {
            Timber.e("Can't create a screen for passed screenKey: `$screenKey`")
        }

        showSystemMessage(UNKNOWN_SCREEN_MESSAGE)
    }

    override fun unexistingActivity(screenKey: String?, activityIntent: Intent?) {
        showSystemMessage(UNKNOWN_SCREEN_MESSAGE)
    }

    private fun createUrlNavigationIntent(url: String): Intent {
        return createNavigationIntent(url, Intent.ACTION_VIEW)
    }

    private fun createNavigationIntent(uri: String, action: String): Intent {
        return Intent(action, Uri.parse(uri))
    }

    companion object {
        private const val UNKNOWN_SCREEN_MESSAGE = "На вашем устройстве нет программ для открытия этой ссылки"
    }
}