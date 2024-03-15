package com.rexrama.githubuser.favorite

import android.app.Application
import com.rexrama.githubuser.database.FavoriteUser
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rexrama.githubuser.repository.FavoriteUserRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)

    val favoriteUserList: LiveData<List<FavoriteUser>> =
        mFavoriteUserRepository.getAllFavoriteUsers()
}