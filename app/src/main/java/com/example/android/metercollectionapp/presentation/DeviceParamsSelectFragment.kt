package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentDeviceParamsSelectBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.DeviceParamsSelectListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.DeviceParamsSelectViewModel
import javax.inject.Inject

class DeviceParamsSelectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _deviceParamsSelectViewModel: DeviceParamsSelectViewModel? = null
    private val deviceParamsSelectViewModel: DeviceParamsSelectViewModel
        get() = _deviceParamsSelectViewModel!!

    private var _binding: FragmentDeviceParamsSelectBinding? = null
    private val binding: FragmentDeviceParamsSelectBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _deviceParamsSelectViewModel = ViewModelProvider(this, viewModelFactory).get(DeviceParamsSelectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_params_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.deviceParamsSelectViewModel = deviceParamsSelectViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        deviceParamsSelectViewModel.setupObjectSelector()

        // отслеживание состояния выбора устройства
        deviceParamsSelectViewModel.selectedDeviceSpinnerPos.observe(viewLifecycleOwner) {
            if (it != null) {
                deviceParamsSelectViewModel.setupParamsForObjectPos(it)
            }
        }

        deviceParamsSelectViewModel.selectObjectUiState.observe(viewLifecycleOwner) { selectUiState ->
            if (selectUiState != null) {
                when {
                    selectUiState.listIsLoading -> {
                        binding.deviceParamsSrChooseDevice.adapter = ArrayAdapter<String>(
                            requireActivity(),  // не activity!!, a requireActivity() для получения контекста
                            R.layout.textview_spinner_item,
                            arrayOf(getString(R.string.state_loading))
                        )
                    }
                    selectUiState.listIsEmpty -> {
                        binding.deviceParamsSrChooseDevice.adapter = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.textview_spinner_item,
                            arrayOf(getString(R.string.device_params_select_no_device))
                        )
                    }
                    selectUiState.listChanged -> {
                        binding.deviceParamsSrChooseDevice.adapter = ArrayAdapter<String>(
                            requireActivity(),
                            R.layout.textview_spinner_item,
                            selectUiState.listOfObjects.map { it.name }
                        )
                    }
                }
            }
        }

        // отслеживание состояния списков
        val leftAdapter = DeviceParamsSelectListAdapter()
        binding.deviceParamsSelectRwLeft.adapter = leftAdapter

        deviceParamsSelectViewModel.availableParamsUiState.observe(viewLifecycleOwner) {
            if (it != null) {
                if (!(it.isLoading || it.isEmpty)) {
                    leftAdapter.submitList(it.paramsUiState)
                }
            }
        }

        val rightAdapter = DeviceParamsSelectListAdapter()
        binding.deviceParamsSelectRwRight.adapter = rightAdapter

        deviceParamsSelectViewModel.selectedParamsUiState.observe(viewLifecycleOwner) {
            if (it != null) {
                if (!(it.isLoading || it.isEmpty)) {
                    rightAdapter.submitList(it.paramsUiState)
                }
            }
        }
    }

    // навигация

}
