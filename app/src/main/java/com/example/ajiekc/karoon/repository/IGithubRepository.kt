package com.example.ajiekc.karoon.repository

import com.example.ajiekc.karoon.db.User
import io.reactivex.Single

interface IGithubRepository {
    fun getUsers(since: Int = 0): Single<List<User>>

    fun getUsersWithFilter(filter: String?): Single<List<User>>
}