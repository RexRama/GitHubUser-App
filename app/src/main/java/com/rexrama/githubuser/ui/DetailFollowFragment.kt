package com.rexrama.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rexrama.githubuser.R
import com.rexrama.githubuser.adapter.UserAdapter
import com.rexrama.githubuser.data.GithubUser
import com.rexrama.githubuser.databinding.FragmentDetailFollowerBinding


class DetailFollowFragment : Fragment() {

    private lateinit var binding: FragmentDetailFollowerBinding
    private lateinit var followViewModel: FollowViewModel
    private var currentTab: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailFollowerBinding.inflate(inflater, container, false)
        followViewModel =
            ViewModelProvider(this@DetailFollowFragment.requireActivity())[FollowViewModel::class.java]

        @Suppress("DEPRECATION")
        val userData: GithubUser? =
            requireActivity().intent.getParcelableExtra(MainActivity.EXTRA_DATA)
        val username: String = userData?.login ?: ""
        val tabTitle = arguments?.getString(TAB_TITLE)
        currentTab = tabTitle
        binding.rvFragmentFollower.layoutManager = LinearLayoutManager(activity)

        if (currentTab == FOLLOWER) {
            followViewModel.displayListFollower(username)
            binding.tvNoData.visibility = View.GONE
        } else if (currentTab == FOLLOWING) {
            binding.tvNoData.visibility = View.GONE
            followViewModel.displayListFollowing(username)

        }

        followViewModel.loading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followViewModel.followerList.observe(viewLifecycleOwner) {
            showRecyclerView()
        }

        followViewModel.followingList.observe(viewLifecycleOwner) {
            showRecyclerView()
        }
        followViewModel.dataNotFound.observe(viewLifecycleOwner) { isDataNotFound ->
            if (isDataNotFound) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvFragmentFollower.visibility = View.GONE
                when (currentTab) {
                    FOLLOWER -> binding.tvNoData.text =
                        resources.getString(R.string.no_follow_found, "Follower")

                    FOLLOWING -> binding.tvNoData.text =
                        resources.getString(R.string.no_follow_found, "Following")
                }
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvFragmentFollower.visibility = View.VISIBLE
            }
        }
        return binding.root
    }

    private fun showRecyclerView() {
        val userList = when (currentTab) {
            FOLLOWER -> followViewModel.followerList.value ?: emptyList()
            FOLLOWING -> followViewModel.followingList.value ?: emptyList()
            else -> emptyList()
        }
        if (userList.isNotEmpty()) {
            binding.rvFragmentFollower.visibility = View.VISIBLE
            binding.tvNoData.visibility = View.GONE
            val userAdapter = UserAdapter(userList)
            binding.rvFragmentFollower.adapter = userAdapter
            userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
                override fun onItemClicked(data: GithubUser) {
                    val toDetail = Intent(requireActivity(), DetailActivity::class.java)
                    toDetail.putExtra(MainActivity.EXTRA_DATA, data)
                    startActivity(toDetail)
                }
            })
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.pbFragmentFollower.visibility = if (loading) View.VISIBLE else View.GONE
    }

    companion object {
        const val TAB_TITLE = "tab_title"
        const val FOLLOWER = "follower"
        const val FOLLOWING = "following"
    }
}