package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.DeviceParamSelectItemBinding
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamSelectUiState

class DeviceParamsSelectListAdapter : ListAdapter<DeviceParamSelectUiState, DeviceParamsSelectListAdapter.SelectParamViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectParamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceParamSelectItemBinding.inflate(layoutInflater, parent, false)
        return SelectParamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectParamViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.deviceParamSelectUiState = item
        holder.binding.executePendingBindings()
    }

    class SelectParamViewHolder (val binding: DeviceParamSelectItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<DeviceParamSelectUiState>() {
        override fun areItemsTheSame(oldItem: DeviceParamSelectUiState, newItem: DeviceParamSelectUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: DeviceParamSelectUiState, newItem: DeviceParamSelectUiState): Boolean {
            return oldItem == newItem
        }
    }

}
