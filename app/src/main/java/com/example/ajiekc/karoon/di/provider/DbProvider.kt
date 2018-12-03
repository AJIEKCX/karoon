package com.example.ajiekc.karoon.di.provider

import android.app.Application
import com.example.ajiekc.karoon.db.AppDatabase
import javax.inject.Inject
import javax.inject.Provider

class DbProvider @Inject constructor(val application: Application) : Provider<AppDatabase> {
    override fun get(): AppDatabase {
        return AppDatabase.getInstance(application)
    }
}