package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.SyncValuesCardBinding
import com.example.android.metercollectionapp.presentation.uistate.SyncValuesCardUiState

class SyncValuesListAdapter : ListAdapter<SyncValuesCardUiState, SyncValuesListAdapter.SyncValueViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SyncValueViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SyncValuesCardBinding.inflate(layoutInflater, parent, false)
        binding.rwRows.adapter = SyncValuesRowsListAdapter()
        return SyncValueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SyncValueViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.cardUiState = item
        (holder.binding.rwRows.adapter as SyncValuesRowsListAdapter).submitList(item.rows)
        holder.binding.executePendingBindings()
    }

    class SyncValueViewHolder (val binding: SyncValuesCardBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<SyncValuesCardUiState>() {
        override fun areItemsTheSame(oldItem: SyncValuesCardUiState, newItem: SyncValuesCardUiState): Boolean {
            return oldItem.guid == newItem.guid
        }
        override fun areContentsTheSame(oldItem: SyncValuesCardUiState, newItem: SyncValuesCardUiState): Boolean {
            return oldItem == newItem
        }
    }

}
