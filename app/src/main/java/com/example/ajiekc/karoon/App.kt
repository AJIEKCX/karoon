package com.example.ajiekc.karoon

import android.app.Application
import com.example.ajiekc.karoon.extensions.isGooglePlayServicesAvailable
import com.google.android.gms.security.ProviderInstaller
import com.vk.sdk.VKSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        if (isGooglePlayServicesAvailable()) {
            ProviderInstaller.installIfNeeded(this)
        }
    }
}