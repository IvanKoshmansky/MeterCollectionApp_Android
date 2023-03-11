package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentScannerBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.viewmodels.ScannerViewModel
import javax.inject.Inject

class ScannerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var scannerViewModel: ScannerViewModel
    private lateinit var binding: FragmentScannerBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerViewModel = ViewModelProvider(this, viewModelFactory).get(ScannerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.scannerViewModel = scannerViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val scanner = QrCodeScanner().apply {
            setup(requireActivity() as AppCompatActivity, binding.scannerView,
            {
                binding.twStatusResult.text = it.text
            },
            {
                it.printStackTrace()
            })
        }
        viewLifecycleOwner.lifecycle.addObserver(scanner)
    }
}
