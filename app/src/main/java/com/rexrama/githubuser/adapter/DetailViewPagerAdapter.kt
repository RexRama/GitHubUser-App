package com.rexrama.githubuser.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rexrama.githubuser.ui.DetailFollowFragment

class DetailViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = DetailFollowFragment()
        val titleTabLayout: String = when (position) {
            0 -> DetailFollowFragment.FOLLOWER
            1 -> DetailFollowFragment.FOLLOWING
            else -> ""
        }
        fragment.arguments = Bundle().apply {
            putString(DetailFollowFragment.TAB_TITLE, titleTabLayout)
        }
        return fragment
    }
}