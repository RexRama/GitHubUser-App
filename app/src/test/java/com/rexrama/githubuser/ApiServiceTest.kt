package com.rexrama.githubuser

import com.rexrama.githubuser.api.ApiService
import com.rexrama.githubuser.data.GithubResponse
import com.rexrama.githubuser.data.GithubUser
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class ApiServiceTest {
    @Mock
    lateinit var apiService: ApiService

    @Test
    fun testSearchGithubUsers() {
        val mockCall = mock(Call::class.java) as Call<GithubResponse>
        `when`(apiService.searchGithubUsers("username", "")).thenReturn(mockCall)

        val expectedResponse = GithubResponse(2, false, emptyList())

        `when`(mockCall.execute()).thenReturn(Response.success(expectedResponse))

        val actualResponse = apiService.searchGithubUsers("username", "").execute()

        assertEquals(expectedResponse, actualResponse.body())

    }

    @Test
    fun testDetailGithubUser() {
        val mockCall = mock(Call::class.java) as Call<GithubUser>
        `when`(apiService.getDetailGithubUser("username", "")).thenReturn(mockCall)

        val expectedResponse = GithubUser(
            "Hahahihi",
            "User",
            "Dimana",
            123456789,
            24,
            "https://avatars.githubusercontent.com/u/90739806?v=4",
            "You know who",
            32,
            21,
            "Godric Hollow"
        )


        `when`(mockCall.execute()).thenReturn(Response.success(expectedResponse))

        val actualResponse = apiService.getDetailGithubUser("username", "").execute()

        assertEquals(expectedResponse, actualResponse.body())
    }


}