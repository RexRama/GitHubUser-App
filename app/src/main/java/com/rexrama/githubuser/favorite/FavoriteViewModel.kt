package com.rexrama.githubuser.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.repository.FavoriteUserRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application, private val pref: SettingPreference) : ViewModel() {
    val isDarkMode : LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    val favoriteUserList: LiveData<List<FavoriteUser>> =
        mFavoriteUserRepository.getAllFavoriteUsers()


    fun checkDarkMode() : Boolean? {
        return isDarkMode.value
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}