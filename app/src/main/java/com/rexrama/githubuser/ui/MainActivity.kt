package com.rexrama.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.UserAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var searchView: SearchView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this@MainActivity)[MainViewModel::class.java]

        viewModel.githubUserList.observe(this) { githubUserList ->
            showRecyclerList(githubUserList)
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        viewModel.dataNotFound.observe(this) {isDataNotFound ->
            if (isDataNotFound) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvGithubUserList.visibility = View.GONE
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvGithubUserList.visibility = View.VISIBLE
            }

        }


        rvUsers = binding.rvGithubUserList
        rvUsers.setHasFixedSize(true)


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
        when(user.itemId) {
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
            else -> return false
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

}



