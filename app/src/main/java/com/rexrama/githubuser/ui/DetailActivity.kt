package com.rexrama.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.DetailViewPagerAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val userData: GithubUser? = intent.getParcelableExtra(MainActivity.EXTRA_DATA)
        val username: String = userData?.login ?: ""
        detailViewModel = ViewModelProvider(this@DetailActivity)[DetailViewModel::class.java]
        detailViewModel.getDetailGithubUser(username)

        detailViewModel.detailGithubUser.observe(this) { detailGithubUser ->
            Glide.with(this)
                .load(detailGithubUser.avatarUrl)
                .into(binding.ivDetailAvatar)
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
    }


    private fun showLoading(loading: Boolean) {
        if (loading) {
            binding.detailProgressBar.visibility = View.VISIBLE
        } else {
            binding.detailProgressBar.visibility = View.GONE
        }
    }
}