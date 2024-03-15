package com.rexrama.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rexrama.githubuser.pref.SettingPreference
import kotlinx.coroutines.launch

class ModeSwitchViewModel(private val pref: SettingPreference) : ViewModel() {
    val isDarkMode : LiveData<Boolean> = pref.getThemeSetting().asLiveData()

    fun checkDarkMode() : Boolean? {
        return isDarkMode.value
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }
}