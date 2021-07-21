package com.example.apollon.viewmodels

import androidx.lifecycle.*
import com.example.apollon.data.DatabaseHelperImpl
import com.example.apollon.data.Resource
import com.example.apollon.data.Result
import com.example.apollon.network.ShortenedLinksRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val shortenedLinksRepo: ShortenedLinksRepo,
    private val dbHelper: DatabaseHelperImpl
) : ViewModel() {
    private val results = MutableLiveData<Resource<List<Result>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            results.postValue(Resource.loading(null))
            try {
                val usersFromDb = dbHelper.getResults()
                if (usersFromDb.isEmpty()) {
                    results.postValue(Resource.error(usersFromDb, "Empty"))
                } else {
                    results.postValue(Resource.success(usersFromDb))
                }

            } catch (e: Exception) {
                results.postValue(Resource.error(null, "Something Went Wrong"))
            }
        }
    }

    fun getShortenedLink(text: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = shortenedLinksRepo.getShortenedLink(text)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getListOfShortenedLink(): LiveData<Resource<List<Result>>> {
        return results
    }

    fun insertShortenedLink(result: Result) {
        viewModelScope.launch {
            try {
                dbHelper.insert(result)
                fetchUsers()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    fun deleteShortenedLink(result: Result) {
        viewModelScope.launch {
            try {
                dbHelper.delete(result)
                fetchUsers()
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

}