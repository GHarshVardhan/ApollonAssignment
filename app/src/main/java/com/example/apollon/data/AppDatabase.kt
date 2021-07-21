package com.example.apollon.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun resultDao(): ResultDao

}