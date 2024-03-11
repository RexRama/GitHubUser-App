package com.rexrama.githubuser.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rexrama.githubuser.BuildConfig
import com.rexrama.githubuser.api.ApiConfig
import com.rexrama.githubuser.data.GithubUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val apiService = ApiConfig.getApiService()
    private val apiKey = BuildConfig.API_KEY

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _followerList = MutableLiveData<List<GithubUser>>()
    val followerList: LiveData<List<GithubUser>> = _followerList

    private val _followingList = MutableLiveData<List<GithubUser>>()
    val followingList: LiveData<List<GithubUser>> = _followingList

    private val _dataNotFound = MutableLiveData<Boolean>()
    val dataNotFound: LiveData<Boolean> = _dataNotFound

    fun displayListFollower(username: String) {
        _loading.value = true
        val client = apiService.getFollowerList(username, apiKey)
        client.enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    if (response.body()?.isEmpty() == true) {
                        _dataNotFound.value = true
                    } else {
                        _dataNotFound.value = false
                        _followerList.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _loading.value = false
            }
        })
    }

    fun displayListFollowing(username: String) {
        _loading.value = true
        val client = apiService.getFollowingList(username, apiKey)
        client.enqueue(object : Callback<List<GithubUser>> {
            override fun onResponse(
                call: Call<List<GithubUser>>,
                response: Response<List<GithubUser>>
            ) {
                if (response.isSuccessful) {
                    _loading.value = false
                    if (response.body()?.isEmpty() == true) {
                        _dataNotFound.value = true
                    } else {
                        _dataNotFound.value = false
                        _followingList.value = response.body()
                    }
                }
            }

            override fun onFailure(call: Call<List<GithubUser>>, t: Throwable) {
                _loading.value = false
            }
        })
    }
}