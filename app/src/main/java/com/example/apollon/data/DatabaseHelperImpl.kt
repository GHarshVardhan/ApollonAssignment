package com.example.apollon.data

class DatabaseHelperImpl(private val appDatabase: AppDatabase) : DatabaseHelper {

    override suspend fun getResults(): List<Result> = appDatabase.resultDao().getAll()

    override suspend fun insert(result: Result) = appDatabase.resultDao().insert(result)

    override suspend fun delete(result: Result) = appDatabase.resultDao().delete(result)

}