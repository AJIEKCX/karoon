package com.example.ajiekc.karoon.di.module

import com.example.ajiekc.karoon.db.AppDatabase
import com.example.ajiekc.karoon.db.AuthDao
import com.example.ajiekc.karoon.di.provider.AuthDaoProvider
import com.example.ajiekc.karoon.di.provider.DbProvider
import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.repository.NewsfeedRepository
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import toothpick.config.Module

class AppModule : Module() {
    init {
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
        bind(AuthRepository::class.java).singletonInScope()
        bind(NewsfeedRepository::class.java).singletonInScope()
        bind(AppDatabase::class.java)
            .toProvider(DbProvider::class.java)
            .providesSingletonInScope()
        bind(AuthDao::class.java)
            .toProvider(AuthDaoProvider::class.java)
            .providesSingletonInScope()
    }
}