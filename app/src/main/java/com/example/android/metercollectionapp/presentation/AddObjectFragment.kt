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
import com.example.android.metercollectionapp.databinding.FragmentAddObjectBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.uistate.AddObjectUiState
import com.example.android.metercollectionapp.presentation.viewmodels.AddObjectViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddObjectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var addObjectViewModel: AddObjectViewModel
    private lateinit var binding: FragmentAddObjectBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addObjectViewModel = ViewModelProvider(this, viewModelFactory).get(AddObjectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_object, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addObjectViewModel = addObjectViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObjectViewModel.navigateUp.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
            }
        }

        addObjectViewModel.navigateToScanner.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(
                    AddObjectFragmentDirections.actionAddObjectFragmentToScannerFragment(
                        ScannerFeature.SCAN_FOR_NEW
                    )
                )
                addObjectViewModel.navigateToScannerDone()
            }
        }

        addObjectViewModel.addObjectUiState.observe(viewLifecycleOwner) {
            when (it.shortMessage) {
                AddObjectUiState.ShortMessageCode.EMPTY_NAME -> Snackbar.make(binding.root,
                    R.string.enter_name_of_new_object, Snackbar.LENGTH_SHORT).show()
                AddObjectUiState.ShortMessageCode.EMPTY_GUID -> Snackbar.make(binding.root,
                    R.string.scan_qr_code_or_enter_object_id, Snackbar.LENGTH_SHORT).show()
                AddObjectUiState.ShortMessageCode.SUCCESS -> Snackbar.make(binding.root,
                    R.string.new_object_save_success, Snackbar.LENGTH_SHORT).show()
                AddObjectUiState.ShortMessageCode.ERROR -> Snackbar.make(binding.root,
                    R.string.new_object_save_error, Snackbar.LENGTH_SHORT).show()
                AddObjectUiState.ShortMessageCode.CAMERA_NOT_GRANTED -> Snackbar.make(binding.root,
                    R.string.camera_permission_not_granted, Snackbar.LENGTH_SHORT).show()
                // обработка дубликатов
                AddObjectUiState.ShortMessageCode.DUBLICATE_GUID -> {}
                else -> {}
            }
        }
    }
}
