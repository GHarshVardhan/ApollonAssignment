package com.example.apollon.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "result")
data class Result(

    @PrimaryKey val code: String,

    val short_link: String,

    val full_short_link: String,

    val short_link2: String,

    val full_short_link2: String,

    val share_link: String,

    val full_share_link: String,

    val original_link: String
)