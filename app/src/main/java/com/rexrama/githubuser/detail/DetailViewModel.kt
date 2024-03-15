package com.rexrama.githubuser.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.rexrama.githubuser.BuildConfig
import com.rexrama.githubuser.api.ApiConfig
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.repository.FavoriteUserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel(application: Application, username: String, pref: SettingPreference) : ViewModel() {
    val isDarkMode : LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    private val apiService = ApiConfig.getApiService()
    private val apiKey = BuildConfig.API_KEY

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _detailGithubUser = MutableLiveData<GithubUser>()
    val detailGithubUser: LiveData<GithubUser> = _detailGithubUser

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)
    val favoriteUserIsExist: LiveData<Boolean> =

        mFavoriteUserRepository.getFavoriteUserByUsername(username)

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

    fun addFavUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.insert(favoriteUser)
    }

    fun deleteFavUser(favoriteUser: FavoriteUser) {
        mFavoriteUserRepository.delete(favoriteUser)
    }

    fun checkFavoriteUserIsExist(): Boolean? {
        return favoriteUserIsExist.value
    }



}