package com.example.ajiekc.karoon.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.ajiekc.karoon.SingletonHolder
import com.example.ajiekc.karoon.entity.AuthData

@Database(entities = arrayOf(AuthData::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun authDao(): AuthDao

    companion object : SingletonHolder<AppDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            AppDatabase::class.java,
            "app_database"
        )
            .build()
    })
}