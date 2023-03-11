package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentSelectObjectBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.viewmodels.SelectObjectViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class SelectObjectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _selectObjectViewModel: SelectObjectViewModel? = null
    private val selectObjectViewModel: SelectObjectViewModel
        get() = _selectObjectViewModel!!

    private lateinit var binding: FragmentSelectObjectBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _selectObjectViewModel = ViewModelProvider(this, viewModelFactory).get(SelectObjectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_object, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.selectObjectViewModel = selectObjectViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectObjectViewModel.navigateToScan.observe(viewLifecycleOwner) {
            if (it == true) {
                findNavController().navigate(
                    SelectObjectFragmentDirections.actionSelectObjectFragmentToScannerFragment()
                )
                selectObjectViewModel.navigateToScanDone()
            }
        }
        selectObjectViewModel.uiState.observe(viewLifecycleOwner) {
            val newState = it ?: return@observe
            when {
                newState.cameraNotGranted -> {
                    Snackbar.make(binding.root, R.string.camera_permission_not_granted, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
