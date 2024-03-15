package com.rexrama.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.DetailViewPagerAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.databinding.ActivityDetailBinding
import com.rexrama.githubuser.helper.ViewModelFactory
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.pref.dataStore
import com.rexrama.githubuser.viewmodel.AddFavoriteViewModel
import com.rexrama.githubuser.viewmodel.DetailViewModel
import com.rexrama.githubuser.viewmodel.ModeSwitchViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var addFavoriteViewModel: AddFavoriteViewModel
    private lateinit var themeViewModel: ModeSwitchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION") val userData: GithubUser? =
            intent.getParcelableExtra(MainActivity.EXTRA_DATA)

        @Suppress("DEPRECATION") val favoriteData: FavoriteUser? =
            intent.getParcelableExtra(FavoriteActivity.EXTRA_FAVORITE)

        val username: String = userData?.login ?: favoriteData?.username.toString()
        val pref = SettingPreference.getInstance(dataStore)

        val viewModelFactory = ViewModelFactory(this@DetailActivity.application, username, pref)
        detailViewModel = ViewModelProvider(
            this@DetailActivity, viewModelFactory
        )[DetailViewModel::class.java]
        detailViewModel.getDetailGithubUser(username)
        addFavoriteViewModel = ViewModelProvider(
            this@DetailActivity, viewModelFactory
        )[AddFavoriteViewModel::class.java]
        themeViewModel = ViewModelProvider(
            this@DetailActivity, viewModelFactory
        )[ModeSwitchViewModel::class.java]



        detailViewModel.detailGithubUser.observe(this) { detailGithubUser ->
            Glide.with(this).load(detailGithubUser.avatarUrl).into(binding.ivDetailAvatar)
            binding.apply {
                tvDetailUsername.text = detailGithubUser.login
                tvDetailName.text = detailGithubUser.name
                tvDetailLocation.text = detailGithubUser.location
                tvDetailCompany.text = detailGithubUser.company
                tvDetailRepo.text =
                    resources.getString(R.string.public_repos, detailGithubUser.publicRepos)
            }
            when {
                detailGithubUser.name == null || detailGithubUser.location == null || detailGithubUser.company == null -> {
                    if (detailGithubUser.name == null) {
                        binding.tvDetailName.visibility = View.GONE
                    }
                    if (detailGithubUser.location == null) {
                        binding.tvDetailLocation.visibility = View.GONE
                    }
                    if (detailGithubUser.company == null) {
                        binding.tvDetailCompany.visibility = View.GONE
                    }
                }
            }
            val detailViewPagerAdapter = DetailViewPagerAdapter(this)
            binding.viewPager.adapter = detailViewPagerAdapter

            TabLayoutMediator(binding.detailFollowerTab, binding.viewPager) { tab, position ->
                tab.text = if (position == 0) {
                    "${detailGithubUser.followers} follower"
                } else {
                    "${detailGithubUser.following} following"
                }
            }.attach()
        }
        detailViewModel.loading.observe(this) {
            showLoading(it)
        }
        themeViewModel.isDarkMode.observe(this) { isDarkMode ->
            val tintResId = if (isDarkMode) {
                R.color.black
            } else {
                R.color.teal
            }
            binding.fabFavorite.backgroundTintList =
                ContextCompat.getColorStateList(this, tintResId)
        }
        addFavoriteViewModel.favoriteUserIsExist.observe(this) { favoriteUserIsExist ->
            val drawableResId = if (favoriteUserIsExist) {
                R.drawable.baseline_favorite_light_24
            } else {
                R.drawable.baseline_favorite_border_light_24
            }

            binding.fabFavorite.setImageDrawable(
                ContextCompat.getDrawable(
                    baseContext, drawableResId
                )
            )
        }

        binding.fabFavorite.setOnClickListener {
            val favoriteUser = favoriteData ?: FavoriteUser(
                id = userData!!.id,
                username = userData.login,
                avatarUrl = userData.avatarUrl,
                type = userData.type,
                publicRepos = binding.tvDetailRepo.text.toString()
            )
            if (addFavoriteViewModel.checkFavoriteUserIsExist()!!) {
                addFavoriteViewModel.deleteFavUser(favoriteUser)
            } else {
                addFavoriteViewModel.addFavUser(favoriteUser)
            }
        }


    }


    private fun showLoading(loading: Boolean) {
        binding.detailProgressBar.visibility = if (loading) View.VISIBLE else View.GONE
    }
}