package com.rexrama.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.UserAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.databinding.ActivityMainBinding
import com.rexrama.githubuser.helper.ViewModelFactory
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.pref.dataStore
import com.rexrama.githubuser.viewmodel.MainViewModel
import com.rexrama.githubuser.viewmodel.ModeSwitchViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var themeViewModel: ModeSwitchViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val pref = SettingPreference.getInstance(dataStore)
        val viewModelFactory = ViewModelFactory(this@MainActivity.application, "", pref)

        viewModel =
            ViewModelProvider(this@MainActivity, viewModelFactory)[MainViewModel::class.java]
        themeViewModel = ViewModelProvider(
            this@MainActivity, viewModelFactory
        )[ModeSwitchViewModel::class.java]
        setAppTheme()

        viewModel.githubUserList.observe(this) { githubUserList ->
            showRecyclerList(githubUserList)
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        viewModel.dataNotFound.observe(this) { isDataNotFound ->
            if (isDataNotFound) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvGithubUserList.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvGithubUserList.visibility = View.VISIBLE
            }

        }

        themeViewModel.isDarkMode.observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        rvUsers = binding.rvGithubUserList
        rvUsers.setHasFixedSize(true)


    }

    private fun setAppTheme() {
        val isDarkModeActive = themeViewModel.checkDarkMode() ?: false
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

    override fun onOptionsItemSelected(user: MenuItem): Boolean {
        when (user.itemId) {
            R.id.action_search -> {
                val searchUser: MenuItem = user
                searchView = searchUser.actionView as SearchView

                searchUser.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        viewModel.displayUserList()
                        return true
                    }

                    override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                        return true
                    }
                })

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.searchGithubUser(query.toString())
                        searchView.clearFocus()
                        return true
                    }
                })
                return true
            }

            R.id.to_favorite -> {
                val toFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(toFavorite)
                return true
            }

            R.id.mode_switch -> {
                val isDarkMode = themeViewModel.checkDarkMode()!!
                themeViewModel.saveThemeSetting(!isDarkMode)
                invalidateOptionsMenu()
                return true
            }

            else -> return false
        }

    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val isDarkMode: Boolean = themeViewModel.checkDarkMode() ?: return true
        val modeIcon = menu?.findItem(R.id.mode_switch)
        val searchIcon = menu?.findItem(R.id.action_search)
        val favIcon = menu?.findItem(R.id.to_favorite)
        if (isDarkMode) {
            modeIcon?.setIcon(R.drawable.baseline_dark_mode_24)
            searchIcon?.setIcon(R.drawable.ic_baseline_search_dark_24)
            favIcon?.setIcon(R.drawable.baseline_favorite_border_dark_24)
        } else {
            modeIcon?.setIcon(R.drawable.baseline_light_24)
            searchIcon?.setIcon(R.drawable.ic_baseline_search_light_24)
            favIcon?.setIcon(R.drawable.baseline_favorite_border_light_24)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}



