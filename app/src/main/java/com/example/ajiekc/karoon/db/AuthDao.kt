package com.example.ajiekc.karoon.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.ajiekc.karoon.entity.AuthData
import io.reactivex.Maybe

@Dao
interface AuthDao {

    @Query("SELECT * FROM auth_table WHERE auth_type=:type")
    fun get(type: String): Maybe<AuthData>

    @Query("SELECT * FROM auth_table")
    fun getAll(): Maybe<List<AuthData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: AuthData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<AuthData>)

    @Query("DELETE FROM auth_table WHERE auth_type=:type")
    fun delete(type: String)

    @Query("DELETE FROM auth_table")
    fun deleteAll()
}