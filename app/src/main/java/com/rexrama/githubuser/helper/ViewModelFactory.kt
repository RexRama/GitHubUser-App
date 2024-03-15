package com.rexrama.githubuser.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.detail.DetailViewModel
import com.rexrama.githubuser.favorite.FavoriteViewModel
import com.rexrama.githubuser.detail.FollowViewModel
import com.rexrama.githubuser.main.MainViewModel

class ViewModelFactory(
    private val application: Application,
    private val username: String,
    private val pref: SettingPreference
) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(application, username, pref) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}