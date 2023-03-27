package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.SyncValueItemBinding
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesElementUiState

class SyncValuesListAdapter : ListAdapter<SyncValuesElementUiState, SyncValuesListAdapter.SyncValueViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncValueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SyncValueItemBinding.inflate(layoutInflater, parent, false)
        return SyncValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncValueViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.uiState = item
        holder.binding.executePendingBindings()
    }

    class SyncValueViewHolder (val binding: SyncValueItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<SyncValuesElementUiState>() {
        override fun areItemsTheSame(oldItem: SyncValuesElementUiState, newItem: SyncValuesElementUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: SyncValuesElementUiState, newItem: SyncValuesElementUiState): Boolean {
            return oldItem == newItem
        }
    }

}
