package com.rexrama.githubuser.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rexrama.githubuser.adapter.FavoriteUserAdapter
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.databinding.ActivityFavoriteBinding
import com.rexrama.githubuser.detail.DetailActivity
import com.rexrama.githubuser.pref.SettingPreference
import com.rexrama.githubuser.pref.dataStore
import com.rexrama.githubuser.repository.FavoriteUserRepository
import com.rexrama.githubuser.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var rvFavoriteUser: RecyclerView
    private lateinit var favoriteViewModel: FavoriteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        rvFavoriteUser = binding.rvFavoriteUserList
        rvFavoriteUser.setHasFixedSize(true)
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

    companion object {
        const val EXTRA_FAVORITE = "favorite_data"
    }
}