package com.example.ajiekc.karoon.api.github

import com.example.ajiekc.karoon.Injection
import com.example.ajiekc.karoon.db.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {

    @GET("users")
    fun getUsers(@Query("since") since: Int? = null): Single<List<User>>

    @GET("users/{login}")
    fun getUserByLogin(@Path("login") login: String): Single<User>

    companion object {
        val instance: GithubService by lazy {
            val retrofit = Injection.provideRetrofit()
            retrofit.create(GithubService::class.java)
        }
    }
}