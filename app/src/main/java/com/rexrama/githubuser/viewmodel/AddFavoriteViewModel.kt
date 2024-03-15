package com.rexrama.githubuser.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.repository.FavoriteUserRepository

class AddFavoriteViewModel(application: Application, username: String) : ViewModel() {
    private val mFavoriteUserRepository: FavoriteUserRepository =
        FavoriteUserRepository(application)
    val favoriteUserIsExist: LiveData<Boolean> =
        mFavoriteUserRepository.getFavoriteUserByUsername(username)

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