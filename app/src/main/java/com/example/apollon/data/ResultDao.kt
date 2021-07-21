package com.example.apollon.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultDao {

    @Query("SELECT * FROM result")
    suspend fun getAll(): List<Result>

    @Insert
    suspend fun insert(result: Result)

    @Delete
    suspend fun delete(result: Result)

}