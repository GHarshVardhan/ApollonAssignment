package com.example.apollon.network

import com.example.apollon.network.ApiService


class ApiHelper(private val apiService: ApiService) {

    suspend fun getShortenedUrl(text: String) = apiService.getShortenedUrl(text)
}