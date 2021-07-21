package com.example.apollon.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apollon.network.ApiHelper
import com.example.apollon.data.DatabaseHelperImpl
import com.example.apollon.network.ShortenedLinksRepo

class ViewModelFactory(private val apiHelper: ApiHelper, private val dbHelper: DatabaseHelperImpl) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(ShortenedLinksRepo(apiHelper),dbHelper) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}