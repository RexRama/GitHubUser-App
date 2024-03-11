package com.rexrama.githubuser.api

import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.data.GithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("search/users")
    fun searchGithubUsers(@Query("q") username: String, @Query("api_key") apiKey: String) : Call<GithubResponse>

    @GET("users")
    fun getGithubUserList(@Query("api_key") apiKey: String) : Call<List<GithubUser>>

    @GET("users/{username}")
    fun getDetailGithubUser(@Path("username") username: String, @Query("api_key") apiKey: String) : Call<GithubUser>

    @GET("users/{username}/followers")
    fun getFollowerList(@Path("username") username: String, @Query("api_key") apiKey: String) : Call<List<GithubUser>>

    @GET("users/{username}/following")
    fun getFollowingList(@Path("username") username: String, @Query("api_key") apiKey: String) : Call<List<GithubUser>>

}