package com.rexrama.githubuser.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.UserAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.databinding.ActivityMainBinding
import com.rexrama.githubuser.detail.DetailActivity
import com.rexrama.githubuser.favorite.FavoriteActivity
import com.rexrama.githubuser.helper.ViewModelFactory
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.pref.dataStore


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreference.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(this@MainActivity.application, "", pref)
        val navigationView = binding.navigationDrawer

        setupNavigation(navigationView)
        searchAction()


        viewModel =
            ViewModelProvider(this@MainActivity, viewModelFactory)[MainViewModel::class.java]
        setAppTheme()

        viewModel.githubUserList.observe(this) { githubUserList ->
            showRecyclerList(githubUserList)
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        viewModel.dataNotFound.observe(this) { isDataNotFound ->
            if (isDataNotFound) {
                Toast.makeText(this@MainActivity, "User Data Not Found", Toast.LENGTH_SHORT).show()
                binding.rvGithubUserList.visibility = View.GONE
            } else {
                binding.rvGithubUserList.visibility = View.VISIBLE
            }

        }

        viewModel.isDarkMode.observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }



        rvUsers = binding.rvGithubUserList
        rvUsers.setHasFixedSize(true)


    }

    private fun setupNavigation(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.to_favorite -> {
                    val toFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                    startActivity(toFavorite)
                    return@setNavigationItemSelectedListener true
                }
                R.id.mode_switch -> {
                    val isDarkMode = viewModel.checkDarkMode()!!
                    viewModel.saveThemeSetting(!isDarkMode)
                    invalidateOptionsMenu()
                    return@setNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    private fun searchAction() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                searchBar.setText(searchView.text)
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    viewModel.searchGithubUser(searchView.text.toString())
                    searchView.clearFocus()
                }
                searchView.hide()
                true
            }
            searchBar.setNavigationOnClickListener { drawerLayout.open() }

        }

    }

    private fun setAppTheme() {
        val isDarkModeActive = viewModel.checkDarkMode() ?: false
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showRecyclerList(githubUserList: List<GithubUser>) {
        rvUsers.layoutManager = LinearLayoutManager(this)
        val userAdapter = UserAdapter(githubUserList)
        rvUsers.adapter = userAdapter

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GithubUser) {
                val toDetail = Intent(this@MainActivity, DetailActivity::class.java)
                toDetail.putExtra(EXTRA_DATA, data)
                startActivity(toDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}



