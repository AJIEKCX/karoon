package com.example.ajiekc.karoon.di.module

import com.example.ajiekc.karoon.repository.AuthRepository
import com.example.ajiekc.karoon.repository.NewsfeedRepository
import toothpick.config.Module

class AppModule : Module() {
    init {
        bind(AuthRepository::class.java).singletonInScope()
        bind(NewsfeedRepository::class.java).singletonInScope()
    }
}