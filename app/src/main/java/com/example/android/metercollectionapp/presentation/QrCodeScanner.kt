package com.example.android.metercollectionapp.presentation

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

/**
 * сканер Qr кодов с привязкой к жизненному циклу компонента
 */

class QrCodeScanner : DefaultLifecycleObserver {

    private var codeScanner: CodeScanner? = null

    fun setup(activity: AppCompatActivity, scannerView: CodeScannerView,
              onDecode: (it: com.google.zxing.Result) -> Unit, onError: (it: Throwable) -> Unit) {
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner?.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                activity.runOnUiThread {
                    Log.d("my_log:", "DecodeCallback")
                    onDecode(it)
                }
            }

            errorCallback = ErrorCallback {
                activity.runOnUiThread {
                    Log.d("my_log:", "ErrorCallback")
                    onError(it)
                }
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        Log.d("my_log:", "QrCodeOnResume")
        codeScanner?.startPreview()
    }

    override fun onPause(owner: LifecycleOwner) {
        Log.d("my_log:", "QrCodeOnPause")
        codeScanner?.releaseResources()
    }
}
