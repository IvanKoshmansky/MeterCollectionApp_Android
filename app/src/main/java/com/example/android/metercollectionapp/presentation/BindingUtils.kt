package com.example.android.metercollectionapp.presentation

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.SyncStatus

@BindingAdapter("app:goneUnless")
fun goneUnless(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}

@BindingAdapter("app:imageViewSyncStatus48")
fun imageViewSyncStatus48(imageView: ImageView, syncStatus: SyncStatus) {
    when (syncStatus) {
        SyncStatus.SUCCESS -> imageView.setImageResource(R.drawable.ic_sync_ok_48)
        SyncStatus.FAILED -> imageView.setImageResource(R.drawable.ic_error_48)
        else -> imageView.setImageResource(R.drawable.ic_sync_48)
    }
}

@BindingAdapter("app:imageViewSyncStatus24")
fun imageViewSyncStatus24(imageView: ImageView, syncStatus: SyncStatus) {
    when (syncStatus) {
        SyncStatus.SUCCESS -> imageView.setImageResource(R.drawable.ic_sync_ok_24)
        SyncStatus.FAILED -> imageView.setImageResource(R.drawable.ic_error_24)
        else -> imageView.setImageResource(R.drawable.ic_sync_24)
    }
}

@BindingAdapter("app:longToText")
fun longToText(textView: TextView, long: Long) {
    textView.text = long.toString()
}

// @InverseBindingAdapter для обратного преобразования
// см. https://developer.android.com/topic/libraries/data-binding/two-way
