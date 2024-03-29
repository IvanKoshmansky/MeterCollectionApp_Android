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
import com.example.android.metercollectionapp.presentation.viewmodels.AddObjectViewModel
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class AddObjectFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var _addObjectViewModel: AddObjectViewModel? = null
    private val addObjectViewModel: AddObjectViewModel
        get() = _addObjectViewModel!!

    private var _binding: FragmentAddObjectBinding? = null
    private val binding: FragmentAddObjectBinding
        get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _addObjectViewModel = ViewModelProvider(this, viewModelFactory).get(AddObjectViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_object, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.addObjectViewModel = addObjectViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addObjectViewModel.navigateUp.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it) {
                    findNavController().navigateUp()
                }
            }
        }

        addObjectViewModel.addObjectUiState.observe(viewLifecycleOwner) {
            if (it != null) {
                when {
                    it.emptyName -> Snackbar.make(binding.root, R.string.snackbar_empty_object_name,
                        Snackbar.LENGTH_SHORT).show()
                    it.emptyGuid -> Snackbar.make(binding.root, R.string.snackbar_empty_object_guid,
                        Snackbar.LENGTH_SHORT).show()
                    it.success -> Snackbar.make(binding.root, R.string.snackbar_new_object_success,
                        Snackbar.LENGTH_SHORT).show()
                    it.error -> Snackbar.make(binding.root, R.string.snackbar_new_object_error,
                        Snackbar.LENGTH_SHORT).show()
                    // обработка дубликатов
                    else -> {}
                }
            }
        }
    }
}
