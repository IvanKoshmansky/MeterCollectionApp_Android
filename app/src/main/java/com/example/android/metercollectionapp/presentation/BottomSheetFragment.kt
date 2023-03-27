package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentBottomSheetBinding

class BottomSheetFragment : Fragment() {

    private lateinit var binding: FragmentBottomSheetBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_sheet, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    fun syncData() {
        Log.d("bottom_sheet_debug", "inSyncData")
    }
}

// TODO: желательно чтобы фрагмент реализовывал интерфейс, который предоставляет нужный функционал
