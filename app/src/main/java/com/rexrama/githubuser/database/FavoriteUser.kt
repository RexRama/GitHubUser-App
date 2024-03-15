package com.rexrama.githubuser.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "image_url")
    var avatarUrl: String? = null,

    @ColumnInfo(name = "username")
    var username: String? = null,

    @ColumnInfo(name = "type")
    var type: String? = null,

    @ColumnInfo(name = "public_repos")
    var publicRepos: String? = null
) : Parcelable