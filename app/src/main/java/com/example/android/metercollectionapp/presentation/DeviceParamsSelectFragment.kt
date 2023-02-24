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
import com.example.android.metercollectionapp.presentation.adapters.SpinnerTextViewAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.DeviceParamsSelectViewModel
import com.google.android.material.snackbar.Snackbar
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

        if (savedInstanceState == null) {
            deviceParamsSelectViewModel.setupObjectsList()
        }

        // отслеживание состояния выбора устройства
        deviceParamsSelectViewModel.selectedDeviceSpinnerPos.observe(viewLifecycleOwner) {
            if (it != null) {
                // отрабатывает в момент перехода в STARTED, в момент присвоения нового адаптера
                // фильтрация от ненужных подгрузок находится внутри данной функции
                deviceParamsSelectViewModel.setupParamsForObjectPos(it)
            }
        }

        val leftAdapter = DeviceParamsSelectListAdapter()
        binding.deviceParamsSelectRwLeft.adapter = leftAdapter
        val rightAdapter = DeviceParamsSelectListAdapter()
        binding.deviceParamsSelectRwRight.adapter = rightAdapter

        // отслеживание изменений UiState
        deviceParamsSelectViewModel.uiState.observe(viewLifecycleOwner) {
            val newState = it ?: return@observe

            if (newState.objectsLoading) {
                binding.deviceParamsSrChooseDevice.adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.textview_spinner_item,
                    arrayOf(getString(R.string.state_loading))
                )
            } else if (newState.objects.isEmpty()) {
                binding.deviceParamsSrChooseDevice.adapter = ArrayAdapter(
                    requireActivity(),
                    R.layout.textview_spinner_item,
                    arrayOf(getString(R.string.device_params_select_no_device))
                )
            } else {
                // TODO: уточнить почему нельзя сделать safe cast с указанием типа обобщения?
                val currentAdapter = binding.deviceParamsSrChooseDevice.adapter as? SpinnerTextViewAdapter<*>
                val needToSet = if (currentAdapter == null) { true } else currentAdapter.currentList != newState.objects
                if (needToSet) {
                    binding.deviceParamsSrChooseDevice.adapter = SpinnerTextViewAdapter(
                        requireActivity(),
                        R.layout.textview_spinner_item,
                        newState.objects
                    ) { item -> item.name }
                    deviceParamsSelectViewModel.selectedDeviceSpinnerPos.value?.let { pos ->
                        binding.deviceParamsSrChooseDevice.setSelection(pos)
                    }
                }
            }

            // эти адаптеры не реагируют на передачу "старого" списка (проверка по ссылке)
            // TODO: уточнить можно ли как-то избежать "анимации" при передаче нового списка адаптеру?
            if (!newState.availableParamsLoading) {
                leftAdapter.submitList(newState.availableParams)
            }
            if (!newState.selectedParamsLoading) {
                rightAdapter.submitList(newState.selectedParams)
            }

        }

        deviceParamsSelectViewModel.saveStatusUiState.observe(viewLifecycleOwner) {
            if (it != null) {
                when {
                    it.saveSuccess -> Snackbar.make(binding.root, R.string.device_params_select_save_success,
                        Snackbar.LENGTH_SHORT).show()
                    it.saveError -> Snackbar.make(binding.root, R.string.device_params_select_save_success,
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}

// по разделению на "до" и "после" создания View: особой разницы нет, можно пользоваться одним
// причина в том, что обозревание начинается уже в состоянии STARTED или RESUMED (надо уточнять),
// View на этот момент в любом случае уже будут созданы
// но корректнее исторически все-таки "обозреватели" помещать в onViewCreated()
