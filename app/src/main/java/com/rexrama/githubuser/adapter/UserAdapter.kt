package com.rexrama.githubuser.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rexrama.githubuser.data.GithubUser

import com.rexrama.githubuser.databinding.ItemRowUserBinding


class UserAdapter(private val githubUserList: List<GithubUser>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    private var onItemClickCallBack: OnItemClickCallback? = null


    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val githubUser = githubUserList[position]
        Glide.with(holder.itemView.context)
            .load(githubUser.avatarUrl)
            .into(holder.binding.cvGithubAvatar)
        holder.apply {
            binding.tvGithubUsername.text = githubUser.login
            binding.tvGithubType.text = githubUser.type
            binding.btnToDetail.setOnClickListener {
                onItemClickCallBack?.onItemClicked(githubUser)
            }
        }
    }

    override fun getItemCount(): Int {
        return githubUserList.size
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallBack = onItemClickCallback
    }

    interface  OnItemClickCallback {
        fun onItemClicked(data: GithubUser)
    }


}