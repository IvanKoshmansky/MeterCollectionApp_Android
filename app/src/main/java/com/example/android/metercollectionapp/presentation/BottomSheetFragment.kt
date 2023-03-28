package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentBottomSheetBinding
import com.example.android.metercollectionapp.databinding.FragmentDeviceParamsListBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.viewmodels.BottomSheetViewModel
import com.example.android.metercollectionapp.presentation.viewmodels.DeviceParamsListViewModel
import javax.inject.Inject

class BottomSheetFragment : Fragment(), BottomSheetContract {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var bottomSheetViewModel: BottomSheetViewModel
    private lateinit var binding: FragmentBottomSheetBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomSheetViewModel = ViewModelProvider(this, viewModelFactory).get(BottomSheetViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    // вызывается из MainActivity при старте вытягивания Bottom Sheet (он находится в FragmentContainerView
    // и управляется через CoordinatorLayout
    override fun onSlideBegin() {
        Log.d("bottom_sheet_debug", "inSyncData")
    }
}

// для ускорения подгрузки возможно потребуется кэширование на уровне репозитория
