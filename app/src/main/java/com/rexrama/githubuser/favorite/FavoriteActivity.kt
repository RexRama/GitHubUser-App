package com.rexrama.githubuser.favorite

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.FavoriteUserAdapter
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.databinding.ActivityFavoriteBinding
import com.rexrama.githubuser.detail.DetailActivity
import com.rexrama.githubuser.helper.ViewModelFactory
import com.rexrama.githubuser.main.MainActivity
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.pref.dataStore
import com.rexrama.githubuser.repository.FavoriteUserRepository

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var rvFavoriteUser: RecyclerView
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val drawerLayout = binding.favDrawerLayout
        val navigationView = binding.navigationDrawer

        binding.topAppBar.setNavigationOnClickListener { drawerLayout.open() }

        setupNavigation(navigationView)

        val pref = SettingPreference.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(this@FavoriteActivity.application, "", pref)
        favoriteViewModel = ViewModelProvider(
            this@FavoriteActivity,
            viewModelFactory
        )[FavoriteViewModel::class.java]
        favoriteViewModel.favoriteUserList.observe(this) { favoriteUser ->
            if (favoriteUser.isEmpty()) {
                binding.rvFavoriteUserList.visibility = View.GONE
                binding.tvNoData.visibility = View.VISIBLE
            } else {
                binding.rvFavoriteUserList.visibility = View.VISIBLE
                binding.tvNoData.visibility = View.GONE
                showFavoriteUserList(favoriteUser)
            }
        }
        favoriteViewModel.isDarkMode.observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }


        rvFavoriteUser = binding.rvFavoriteUserList
        rvFavoriteUser.setHasFixedSize(true)
    }


    private fun setupNavigation(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.to_home -> {
                    val toFavorite = Intent(this@FavoriteActivity, MainActivity::class.java)
                    startActivity(toFavorite)
                    return@setNavigationItemSelectedListener true
                }

                R.id.mode_switch -> {
                    val isDarkMode = favoriteViewModel.checkDarkMode()!!
                    favoriteViewModel.saveThemeSetting(!isDarkMode)
                    invalidateOptionsMenu()
                    return@setNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }

    private fun showFavoriteUserList(favoriteUser: List<FavoriteUser>) {
        rvFavoriteUser.layoutManager = LinearLayoutManager(this)
        val favoriteAdapter = FavoriteUserAdapter(
            favoriteUser,
            FavoriteUserRepository(this@FavoriteActivity.application)
        )
        rvFavoriteUser.adapter = favoriteAdapter

        favoriteAdapter.setOnItemClickCallback(object : FavoriteUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: FavoriteUser) {
                val toDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                toDetail.putExtra(EXTRA_FAVORITE, data)
                startActivity(toDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    companion object {
        const val EXTRA_FAVORITE = "favorite_data"
    }
}