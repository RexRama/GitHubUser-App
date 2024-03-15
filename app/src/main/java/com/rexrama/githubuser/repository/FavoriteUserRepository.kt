package com.rexrama.githubuser.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.database.FavoriteItemUserDao
import com.rexrama.githubuser.database.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUserRepository(application: Application) {
    private val mFavoriteItemUserDao: FavoriteItemUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteItemUserDao = db.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteItemUserDao.getAllFavoriteUser()

    fun insert(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteItemUserDao.insert(favoriteUser) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<Boolean> =
        mFavoriteItemUserDao.findFavoriteUserByUsername(username)

    fun delete(favoriteUser: FavoriteUser) {
        executorService.execute { mFavoriteItemUserDao.delete(favoriteUser) }
    }
}