package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.metercollectionapp.domain.Repository
import javax.inject.Inject

class SelectObjectViewModel @Inject constructor (private val repository: Repository) : ViewModel() {

    private val _navigateToScan = MutableLiveData(false)
    val navigateToScan: LiveData<Boolean>
        get() = _navigateToScan

    fun navigateToScanDone() {
        _navigateToScan.value = false
    }

    fun onScan() {
        _navigateToScan.value = true
    }

}
