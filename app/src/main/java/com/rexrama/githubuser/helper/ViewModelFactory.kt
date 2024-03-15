package com.rexrama.githubuser.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.viewmodel.AddFavoriteViewModel
import com.rexrama.githubuser.viewmodel.DetailViewModel
import com.rexrama.githubuser.viewmodel.FavoriteViewModel
import com.rexrama.githubuser.viewmodel.FollowViewModel
import com.rexrama.githubuser.viewmodel.MainViewModel
import com.rexrama.githubuser.viewmodel.ModeSwitchViewModel

class ViewModelFactory(
    private val application: Application,
    private val username: String,
    private val pref: SettingPreference
) : ViewModelProvider.NewInstanceFactory() {


    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModeSwitchViewModel::class.java)) {
            return ModeSwitchViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel() as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel() as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FollowViewModel::class.java)) {
            return FollowViewModel() as T
        } else if (modelClass.isAssignableFrom(AddFavoriteViewModel::class.java)) {
            return AddFavoriteViewModel(application, username) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}