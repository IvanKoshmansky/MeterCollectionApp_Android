package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.ObjectItemBinding
import com.example.android.metercollectionapp.presentation.uistate.ObjectUiState

class ObjectsListAdapter (
    private val objectClickListener: ObjectClickListener?
) : ListAdapter<ObjectUiState, ObjectsListAdapter.ObjectViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ObjectItemBinding.inflate(layoutInflater, parent, false)
        return ObjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.objectUiState = item
        if (objectClickListener != null) {
            holder.binding.clickListener = objectClickListener
        }
        holder.binding.executePendingBindings()
    }

    class ObjectViewHolder (val binding: ObjectItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<ObjectUiState>() {
        override fun areItemsTheSame(oldItem: ObjectUiState, newItem: ObjectUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: ObjectUiState, newItem: ObjectUiState): Boolean {
            return oldItem == newItem
        }
    }

}

class ObjectClickListener (val clickLambda: (id: Long) -> Unit) {
    fun onClick(objectUiState: ObjectUiState) {
        clickLambda(objectUiState.uid)
    }
}
