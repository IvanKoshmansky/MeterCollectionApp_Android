package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.WriteValueItemBinding
import com.example.android.metercollectionapp.presentation.uistate.WriteValuesElementUiState

class WriteValuesListAdapter : ListAdapter<WriteValuesElementUiState, WriteValuesListAdapter.WriteValuesElementViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteValuesElementViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = WriteValueItemBinding.inflate(layoutInflater, parent, false)
        return WriteValuesElementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WriteValuesElementViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.uiState = item
        holder.binding.iwDeleteElement.setOnClickListener {
            item.deleteElementLambda(item.uid)
        }
        holder.binding.executePendingBindings()
    }

    class WriteValuesElementViewHolder (val binding: WriteValueItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<WriteValuesElementUiState>() {
        override fun areItemsTheSame(oldItem: WriteValuesElementUiState, newItem: WriteValuesElementUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: WriteValuesElementUiState, newItem: WriteValuesElementUiState): Boolean {
            return oldItem == newItem
        }
    }

}
