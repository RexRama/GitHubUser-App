package com.rexrama.githubuser.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class GithubResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<GithubUser>
)

@Parcelize
data class GithubUser(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("public_repos")
    val publicRepos: Int?,

    @field:SerializedName("avatar_url")
    val avatarUrl: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("followers")
    val followers: Int?,

    @field:SerializedName("following")
    val following: Int?,

    @field:SerializedName("location")
    val location: String?,


    ) : Parcelable
