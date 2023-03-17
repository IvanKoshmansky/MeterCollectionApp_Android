package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentDeviceParamsListBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.DeviceParamsListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.DeviceParamsListViewModel
import javax.inject.Inject

class DeviceParamsListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var deviceParamsListViewModel: DeviceParamsListViewModel
    private lateinit var binding: FragmentDeviceParamsListBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceParamsListViewModel =
            ViewModelProvider(this, viewModelFactory).get(DeviceParamsListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_params_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.deviceParamsListViewModel = deviceParamsListViewModel

        val adapter = DeviceParamsListAdapter()
        binding.rwParams.adapter = adapter
        deviceParamsListViewModel.uiState.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                adapter.submitList(it.paramsUiState)
            }
        }

        deviceParamsListViewModel.navigateToAdd.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    DeviceParamsListFragmentDirections.actionDeviceParamsListFragmentToAddDeviceParamFragment()
                )
                deviceParamsListViewModel.navigateToAddDone()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceParamsListViewModel.setup()
    }
}
