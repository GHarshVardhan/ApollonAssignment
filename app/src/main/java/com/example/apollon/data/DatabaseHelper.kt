package com.example.apollon.data

interface DatabaseHelper {

    suspend fun getResults(): List<Result>

    suspend fun insert(result: Result)

    suspend fun delete(result: Result)

}