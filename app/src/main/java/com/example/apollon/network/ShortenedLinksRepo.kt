package com.example.apollon.network

class ShortenedLinksRepo(private val apiHelper: ApiHelper) {
    suspend fun getShortenedLink(text: String) = apiHelper.getShortenedUrl(text)
}
