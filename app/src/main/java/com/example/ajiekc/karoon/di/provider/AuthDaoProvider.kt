package com.example.ajiekc.karoon.di.provider

import com.example.ajiekc.karoon.db.AppDatabase
import com.example.ajiekc.karoon.db.AuthDao
import javax.inject.Inject
import javax.inject.Provider

class AuthDaoProvider @Inject constructor(val database: AppDatabase) : Provider<AuthDao> {
    override fun get(): AuthDao {
        return database.authDao()
    }
}