package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.databinding.FragmentDeviceParamsSelectBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.DeviceParamsSelectListAdapter
import com.example.android.metercollectionapp.presentation.adapters.SpinnerTextViewAdapter
import com.example.android.metercollectionapp.presentation.uistate.DeviceParamsSelectSaveStatusUiState
import com.example.android.metercollectionapp.presentation.uistate.ObjectUiState
import com.example.android.metercollectionapp.presentation.viewmodels.DeviceParamsSelectViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class DeviceParamsSelectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var deviceParamsSelectViewModel: DeviceParamsSelectViewModel
    private lateinit var binding: FragmentDeviceParamsSelectBinding

    private lateinit var spinnerAdapter: SpinnerTextViewAdapter<ObjectUiState>
    private lateinit var leftAdapter: DeviceParamsSelectListAdapter
    private lateinit var rightAdapter: DeviceParamsSelectListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        deviceParamsSelectViewModel =
            ViewModelProvider(this, viewModelFactory).get(DeviceParamsSelectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_params_select, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.deviceParamsSelectViewModel = deviceParamsSelectViewModel

        // разделяем конструирование интерфейса (здесь) и присвоение новых списков адаптерам (в
        // onViewCreated()
        spinnerAdapter = SpinnerTextViewAdapter(requireContext(), R.layout.textview_spinner_item) {
            it.name
        }
        binding.srSelectDevice.adapter = spinnerAdapter

        leftAdapter = DeviceParamsSelectListAdapter()
        binding.rwLeft.adapter = leftAdapter
        rightAdapter = DeviceParamsSelectListAdapter()
        binding.rwRight.adapter = rightAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            deviceParamsSelectViewModel.setupObjectsList()
        }

        // отслеживание состояния выбора устройства
        deviceParamsSelectViewModel.selectedDeviceSpinnerPos.observe(viewLifecycleOwner) {
            // отрабатывает в момент перехода жизненного цикла в состояние STARTED
            // фильтрация от ненужных подгрузок находится внутри данной функции
            deviceParamsSelectViewModel.setupParamsForObjectPos(it)
        }

        // отслеживание изменений UiState
        deviceParamsSelectViewModel.uiState.observe(viewLifecycleOwner) { newState ->
            // здесь только отправка новых данных адапетрам, причем SpinnerAdapter не чувствителен
            // к передаче ссылки на один и тот же список
            when {
                newState.objectsLoading -> spinnerAdapter.submitList(listOf(ObjectUiState(0, SyncStatus.UNKNOWN,
                    getString(R.string.loading))))
                newState.objects.isEmpty() -> spinnerAdapter.submitList(listOf(ObjectUiState(0, SyncStatus.UNKNOWN,
                    getString(R.string.no_devices))))
                else -> spinnerAdapter.submitList(newState.objects)
            }
            if (!newState.availableParamsLoading) {
                leftAdapter.submitList(newState.availableParams)
            }
            if (!newState.selectedParamsLoading) {
                rightAdapter.submitList(newState.selectedParams)
            }
        }

        deviceParamsSelectViewModel.saveStatusUiState.observe(viewLifecycleOwner) {
            when (it.shortMessage) {
                DeviceParamsSelectSaveStatusUiState.ShortMessageCode.SUCCESS -> Snackbar.make(binding.root,
                    R.string.save_success, Snackbar.LENGTH_SHORT).show()
                DeviceParamsSelectSaveStatusUiState.ShortMessageCode.ERROR -> Snackbar.make(binding.root,
                    R.string.save_error, Snackbar.LENGTH_SHORT).show()
                else -> {}
            }
        }
    }
}

// по разделению на "до" и "после" создания View: особой разницы нет, можно пользоваться одним
// причина в том, что обозревание начинается уже в состоянии STARTED или RESUMED (надо уточнять),
// View на этот момент в любом случае уже будут созданы
// но корректнее "исторически" все-таки обозреватели помещать в onViewCreated()
