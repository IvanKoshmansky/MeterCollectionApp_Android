package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.UserItemBinding
import com.example.android.metercollectionapp.presentation.uistate.UserUiState

class UsersListAdapter (
    private val userItemClickListener: UserClickListener
) : ListAdapter<UserUiState, UsersListAdapter.UserViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = UserItemBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.userUiState = item
        holder.binding.clickListener = userItemClickListener
        holder.binding.executePendingBindings()
    }

    class UserViewHolder (val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<UserUiState>() {
        override fun areItemsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: UserUiState, newItem: UserUiState): Boolean {
            return oldItem == newItem
        }
    }

}

class UserClickListener (val clickLambda: (id: Long) -> Unit) {
    fun onClick(userUiState: UserUiState) {
        clickLambda(userUiState.id)
    }
}
