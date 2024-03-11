package com.rexrama.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rexrama.githubuser.BuildConfig
import com.rexrama.githubuser.api.ApiConfig
import com.rexrama.githubuser.data.GithubResponse
import com.rexrama.githubuser.data.GithubUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val apiKey = BuildConfig.API_KEY

    private val _githubUserList = MutableLiveData<List<GithubUser>>()
    val githubUserList : LiveData<List<GithubUser>> = _githubUserList

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _dataNotFound = MutableLiveData<Boolean>()
    val dataNotFound : LiveData<Boolean> = _dataNotFound

    init {
        displayUserList()
    }

    fun displayUserList() {
        _loading.value = true
        val client = apiService.getGithubUserList(apiKey)
        client.enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _githubUserList.value = response.body()
                } else {
                    _loading.value = false
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _loading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun searchGithubUser(username : String) {
        _loading.value = true
        val client = apiService.searchGithubUsers(username, apiKey)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    if (response.body()?.items?.isEmpty() == true) {
                        _dataNotFound.value = true
                    } else {
                        _dataNotFound.value = false
                        _githubUserList.value = response.body()?.items
                    }
                } else {
                    _loading.value = false
                    Log.e("MainViewModel", "Failed to retrieve data: ${response.message()}")

                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _loading.value = false
                Log.e("MainViewModel", "Network request failed: ${t.message}")
            }
        })
    }

}
