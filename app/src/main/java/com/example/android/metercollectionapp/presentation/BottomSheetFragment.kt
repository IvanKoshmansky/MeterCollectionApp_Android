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
import com.example.android.metercollectionapp.databinding.FragmentBottomSheetBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.adapters.SyncValuesListAdapter
import com.example.android.metercollectionapp.presentation.viewmodels.BottomSheetViewModel
import javax.inject.Inject

class BottomSheetFragment : Fragment(), BottomSheetContract {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var bottomSheetViewModel: BottomSheetViewModel
    private lateinit var binding: FragmentBottomSheetBinding
    private lateinit var adapter: SyncValuesListAdapter

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
        adapter = SyncValuesListAdapter()
        binding.rwValueCards.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetViewModel.uiState.observe(viewLifecycleOwner) {
            if (it.userLoggedIn) {
                binding.twUserName.text = it.userName
            } else {
                binding.twUserName.text = getText(R.string.user_not_selected)
            }

            if (!it.isLoading) {
                adapter.submitList(it.values)
            }
        }
    }

    // вызывается из MainActivity при старте вытягивания Bottom Sheet (он находится в FragmentContainerView
    // и управляется через CoordinatorLayout
    override fun onSlideBegin() {
        bottomSheetViewModel.setup()
    }
}

// для ускорения подгрузки возможно потребуется кэширование на уровне репозитория
