package com.rexrama.githubuser.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rexrama.githubuser.BuildConfig
import com.rexrama.githubuser.api.ApiConfig
import com.rexrama.githubuser.data.GithubUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val apiKey = BuildConfig.API_KEY

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private val _detailGithubUser = MutableLiveData<GithubUser>()
    val detailGithubUser : LiveData<GithubUser> = _detailGithubUser

    fun getDetailGithubUser(username: String) {
        _loading.value = true
        val client = apiService.getDetailGithubUser(username, apiKey)
        client.enqueue(object : Callback<GithubUser> {
            override fun onResponse(call: Call<GithubUser>, response: Response<GithubUser>) {
                if (response.isSuccessful) {
                    _loading.value = false
                    _detailGithubUser.value = response.body()
                }
            }

            override fun onFailure(call: Call<GithubUser>, t: Throwable) {
                _loading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }
}