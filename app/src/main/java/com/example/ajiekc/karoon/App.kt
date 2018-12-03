package com.example.ajiekc.karoon

import android.app.Application
import com.example.ajiekc.karoon.di.module.AppModule
import com.example.ajiekc.karoon.di.module.ServerModule
import com.example.ajiekc.karoon.extensions.PreferenceHelper.DEFAULT_PREFERENCES
import com.example.ajiekc.karoon.extensions.isGooglePlayServicesAvailable
import com.google.android.gms.security.ProviderInstaller
import com.vk.sdk.VKSdk
import timber.log.Timber
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.smoothie.module.SmoothieApplicationModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initLogger()
        initOAuthSDK()
        initIoc()
    }

    private fun initOAuthSDK() {
        VKSdk.initialize(this)
        if (isGooglePlayServicesAvailable()) {
            ProviderInstaller.installIfNeeded(this)
        }
    }

    private fun initLogger() {
        when {
            BuildConfig.DEBUG -> Timber.plant(Timber.DebugTree())
            else -> Timber.plant(object : Timber.Tree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

                }
            })
        }
    }

    private fun initIoc() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction().disableReflection())
        }

        val appScope = Toothpick.openScope(this)
        appScope.installModules(SmoothieApplicationModule(this, DEFAULT_PREFERENCES), ServerModule(), AppModule())
    }
}