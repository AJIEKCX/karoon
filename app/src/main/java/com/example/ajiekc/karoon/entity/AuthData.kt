package com.example.ajiekc.karoon.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "auth_table")
data class AuthData(
    @PrimaryKey
    @ColumnInfo(name = "auth_type")
    var authType: String = "",
    @ColumnInfo(name = "first_name")
    var firstName: String? = null,
    @ColumnInfo(name = "last_name")
    var lastName: String? = null,
    @ColumnInfo(name = "photo_url")
    var photoUrl: String? = null,
    @ColumnInfo(name = "access_token")
    var accessToken: String? = null
)
