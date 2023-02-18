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
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentAddDeviceParamBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.domain.model.DeviceParamType
import com.example.android.metercollectionapp.presentation.viewmodels.AddDeviceParamViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddDeviceParamFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _addDeviceParamViewModel: AddDeviceParamViewModel? = null
    private val addDeviceParamViewModel: AddDeviceParamViewModel
        get() = _addDeviceParamViewModel!!

    private var _binding: FragmentAddDeviceParamBinding? = null
    private val binding: FragmentAddDeviceParamBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _addDeviceParamViewModel = ViewModelProvider(this, viewModelFactory).get(AddDeviceParamViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_device_param, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addDeviceParamViewModel = addDeviceParamViewModel

        // для Spinner'а с кастомными элементами можно использовать BaseAdapter
        binding.addDeviceParamSrDataType.adapter = ArrayAdapter<String>(
            requireActivity(),  // не activity!!, a requireActivity() для получения контекста
            R.layout.textview_spinner_item,
            DeviceParamType.values().map { it.name }.toTypedArray()
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDeviceParamViewModel.navigateUp.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    findNavController().navigateUp()
                }
            }
        }

        addDeviceParamViewModel.addParamUiState.observe(viewLifecycleOwner) {
            it?.let {
                when {
                    it.emptyFields -> Snackbar.make(binding.root, R.string.snackbar_empty_param_fields,
                        Snackbar.LENGTH_SHORT).show()
                    it.success -> Snackbar.make(binding.root, R.string.snackbar_new_device_param_success,
                        Snackbar.LENGTH_SHORT).show()
                    it.error -> Snackbar.make(binding.root, R.string.snackbar_new_device_param_error,
                        Snackbar.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }
    }
}
