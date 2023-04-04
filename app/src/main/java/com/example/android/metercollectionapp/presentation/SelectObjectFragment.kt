package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentSelectObjectBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.ObjectsListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.SelectObjectViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SelectObjectFragment : Fragment(), BeforeNavigateUpContract {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var selectObjectViewModel: SelectObjectViewModel
    private lateinit var binding: FragmentSelectObjectBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        selectObjectViewModel = ViewModelProvider(this, viewModelFactory).get(SelectObjectViewModel::class.java)

        // перехват СИСТЕМНОЙ кнопки Back на устройстве
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                beforeNavigateUp()
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_object, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.selectObjectViewModel = selectObjectViewModel

        val adapter = ObjectsListAdapter(null)
        binding.rwObjects.adapter = adapter
        selectObjectViewModel.uiState.observe(viewLifecycleOwner) {
            if (!it.isLoading) {
                adapter.submitList(it.objects)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectObjectViewModel.setup()

        selectObjectViewModel.navigateToScan.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    SelectObjectFragmentDirections.actionSelectObjectFragmentToScannerFragment(
                        ScannerFeature.SCAN_FOR_EXISTING
                    )
                )
                selectObjectViewModel.navigateToScanDone()
            }
        }

        selectObjectViewModel.uiState.observe(viewLifecycleOwner) { newState ->
            if (newState.cameraNotGranted) {
                Snackbar.make(binding.root, R.string.camera_permission_not_granted,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    // обработка ДО перехода по навигации наверх в том числе через Toolbar
    override fun beforeNavigateUp() {
        selectObjectViewModel.userLogOut()
    }

}
