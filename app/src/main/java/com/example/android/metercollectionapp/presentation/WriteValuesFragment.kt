package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentWriteValuesBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.domain.model.DeviceParamType
import com.example.android.metercollectionapp.presentation.adapters.SpinnerTextViewAdapter
import com.example.android.metercollectionapp.presentation.adapters.WriteValuesListAdapter
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamUiState
import com.example.android.metercollectionapp.presentation.viewmodels.WriteValuesViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class WriteValuesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var writeValuesViewModel: WriteValuesViewModel
    private lateinit var binding: FragmentWriteValuesBinding

    private lateinit var spinnerAdapter: SpinnerTextViewAdapter<DeviceParamUiState>
    private lateinit var writeValuesAdapter: WriteValuesListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        writeValuesViewModel = ViewModelProvider(this, viewModelFactory).get(WriteValuesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_write_values, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.writeValuesViewModel = writeValuesViewModel
        spinnerAdapter = SpinnerTextViewAdapter(requireContext(), R.layout.textview_spinner_item) {
            it.name
        }
        binding.srSelectParam.adapter = spinnerAdapter
        writeValuesAdapter = WriteValuesListAdapter()
        binding.rwValuesTable.adapter = writeValuesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            val arguments = WriteValuesFragmentArgs.fromBundle(requireArguments())
            writeValuesViewModel.setup(arguments.guid, arguments.name)
        }

        writeValuesViewModel.selectedParamIndex.observe(viewLifecycleOwner) {
            // выбрать параметр из списка по позиции в списке
            writeValuesViewModel.selectParamByIndex(it)
        }

        writeValuesViewModel.uiState.observe(viewLifecycleOwner) { newState ->
            when {
                newState.deviceParams.isLoading -> spinnerAdapter.submitList(
                    listOf(DeviceParamUiState(name = getString(R.string.loading)))
                )
                newState.deviceParams.params.isEmpty() -> spinnerAdapter.submitList(
                    listOf(DeviceParamUiState(name = getString(R.string.no_associated_params)))
                )
                else -> spinnerAdapter.submitList(newState.deviceParams.params)
                // адаптер не чувствителен к передаче одной и той же ссылки
            }
            binding.twParamShortName.text = getString(R.string.placeholder_equals_sign,
                    newState.selectedParamShortName)
            binding.etParamValue.inputType = when (newState.selectedParamType) {
                DeviceParamType.UINT32 -> { InputType.TYPE_CLASS_NUMBER }
                DeviceParamType.INT32 -> { InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED }
                DeviceParamType.FLOAT -> { InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
            }
            writeValuesAdapter.submitList(newState.enteredValues)
            // сообщения в snackbar
            if (newState.alreadyEntered) {
                Snackbar.make(binding.root, R.string.value_already_entered, Snackbar.LENGTH_SHORT).show()
            }
            if (newState.convError) {
                Snackbar.make(binding.root, R.string.incorrect_value, Snackbar.LENGTH_SHORT).show()
            }
            // имя устройства присваивается в xml
        }
    }
}

// в зависимости от типа данных должен динамически меняться InputType у EditText, при этом все значения всех типов
// храняться и передаются в формате FLOAT
// разделение: создание адаптеров в onCreateView(), присвоение списков адаптерам в onViewCreated()
