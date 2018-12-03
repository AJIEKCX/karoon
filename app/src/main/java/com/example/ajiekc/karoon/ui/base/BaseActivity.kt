package com.example.ajiekc.karoon.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.SupportAppNavigator
import toothpick.Scope
import toothpick.Toothpick

abstract class BaseActivity : AppCompatActivity() {

    protected abstract val navigator: SupportAppNavigator

    protected val scope: Scope by lazy(LazyThreadSafetyMode.NONE) {
        Toothpick.openScopes(application, this)
    }

    private val navigationHolder: NavigatorHolder by lazy(LazyThreadSafetyMode.NONE) {
        scope.getInstance(NavigatorHolder::class.java)
    }

    override fun onResume() {
        super.onResume()

        navigationHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        navigationHolder.removeNavigator()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            Toothpick.closeScope(this)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    protected inline fun <reified T : ViewModel> createViewModel(): T {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return scope.getInstance(modelClass)
            }
        }

        return ViewModelProviders.of(this, factory).get(T::class.java)
    }
}