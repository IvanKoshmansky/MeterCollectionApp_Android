package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.SyncValuesRowItemBinding
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesRowUiState

class SyncValuesRowsListAdapter :
    ListAdapter<SyncValuesRowUiState, SyncValuesRowsListAdapter.SyncValuesRowViewHolder> (DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncValuesRowViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SyncValuesRowItemBinding.inflate(layoutInflater, parent, false)
        return SyncValuesRowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncValuesRowViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.uiState = item
        holder.binding.executePendingBindings()
    }

    class SyncValuesRowViewHolder (val binding: SyncValuesRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<SyncValuesRowUiState>() {
        override fun areItemsTheSame(oldItem: SyncValuesRowUiState, newItem: SyncValuesRowUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: SyncValuesRowUiState, newItem: SyncValuesRowUiState): Boolean {
            return oldItem == newItem
        }
    }

}
