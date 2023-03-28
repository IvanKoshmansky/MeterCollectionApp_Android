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
import com.example.android.metercollectionapp.domain.model.DeviceParam
import com.example.android.metercollectionapp.presentation.uistate.AddDeviceParamUiState
import com.example.android.metercollectionapp.presentation.viewmodels.AddDeviceParamViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddDeviceParamFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var addDeviceParamViewModel: AddDeviceParamViewModel
    private lateinit var binding: FragmentAddDeviceParamBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addDeviceParamViewModel = ViewModelProvider(this, viewModelFactory).get(AddDeviceParamViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_device_param, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addDeviceParamViewModel = addDeviceParamViewModel

        binding.srDataType.adapter = ArrayAdapter(
            requireActivity(),
            R.layout.textview_spinner_item,
            DeviceParam.ParamType.values().map { it.name }
        )
        // для простейших применений spinner можно использовать ArrayAdapter, но ArrayAdapter не дает доступ к списку,
        // с которым он сейчас работает, здесь применен BaseAdapter чтобы иметь такой доступ

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addDeviceParamViewModel.navigateUp.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }

        addDeviceParamViewModel.addParamUiState.observe(viewLifecycleOwner) {
            when (it.shortMessage) {
                AddDeviceParamUiState.ShortMessageCode.EMPTY_FIELDS -> Snackbar.make(binding.root,
                    R.string.fill_all_fields, Snackbar.LENGTH_SHORT).show()
                AddDeviceParamUiState.ShortMessageCode.SUCCESS -> Snackbar.make(binding.root,
                    R.string.new_param_save_success, Snackbar.LENGTH_SHORT).show()
                AddDeviceParamUiState.ShortMessageCode.ERROR -> Snackbar.make(binding.root,
                    R.string.new_param_save_error, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}
