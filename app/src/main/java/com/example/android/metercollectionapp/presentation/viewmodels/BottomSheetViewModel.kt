package com.example.android.metercollectionapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.UserManager
import javax.inject.Inject

class BottomSheetViewModel @Inject constructor (
    private val repository: Repository,
    private val userManager: UserManager
) : ViewModel() {

}
