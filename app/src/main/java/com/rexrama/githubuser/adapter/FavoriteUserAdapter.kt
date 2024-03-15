package com.rexrama.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rexrama.githubuser.database.FavoriteUser
import com.rexrama.githubuser.databinding.ItemFavoriteUserBinding
import com.rexrama.githubuser.repository.FavoriteUserRepository

class FavoriteUserAdapter(
    private val favoriteUserList: List<FavoriteUser>,
    private val favoriteUserRepository: FavoriteUserRepository
) : RecyclerView.Adapter<FavoriteUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    class ListViewHolder(var binding: ItemFavoriteUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemFavoriteUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return favoriteUserList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val favoriteUser = favoriteUserList[position]
        Glide.with(holder.itemView.context)
            .load(favoriteUser.avatarUrl)
            .into(holder.binding.cvGithubAvatar)
        holder.apply {
            binding.tvGithubUsername.text = favoriteUser.username
            binding.tvGithubType.text = favoriteUser.type
            binding.tvDetailRepo.text = favoriteUser.publicRepos
            binding.btnToDetail.setOnClickListener {
                @Suppress("DEPRECATION")
                onItemClickCallback?.onItemClicked(favoriteUserList[holder.adapterPosition])
            }
            binding.btnDelete.setOnClickListener {
                favoriteUserRepository.delete(favoriteUser)
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteUser)
    }
}