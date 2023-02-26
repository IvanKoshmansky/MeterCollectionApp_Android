package com.example.android.metercollectionapp.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.metercollectionapp.databinding.DeviceParamItemBinding
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamUiState

class DeviceParamsListAdapter : ListAdapter<DeviceParamUiState, DeviceParamsListAdapter.ParamViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParamViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DeviceParamItemBinding.inflate(layoutInflater, parent, false)
        return ParamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParamViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.deviceParamUiState = item
        holder.binding.executePendingBindings()
    }

    class ParamViewHolder (val binding: DeviceParamItemBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback: DiffUtil.ItemCallback<DeviceParamUiState>() {
        override fun areItemsTheSame(oldItem: DeviceParamUiState, newItem: DeviceParamUiState): Boolean {
            return oldItem.uid == newItem.uid
        }
        override fun areContentsTheSame(oldItem: DeviceParamUiState, newItem: DeviceParamUiState): Boolean {
            return oldItem == newItem
        }
    }

}
