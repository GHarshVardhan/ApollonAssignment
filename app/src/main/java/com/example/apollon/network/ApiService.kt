package com.example.apollon.network

import com.example.apollon.data.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("shorten")
    suspend fun getShortenedUrl(@Query("url")url: String): Response

}