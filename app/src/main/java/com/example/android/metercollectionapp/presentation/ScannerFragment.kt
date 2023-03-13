package com.example.android.metercollectionapp.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentScannerBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.presentation.viewmodels.ScannerViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

class ScannerFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var scannerViewModel: ScannerViewModel
    private lateinit var binding: FragmentScannerBinding

    private lateinit var cameraExecutor: ExecutorService

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (getContext()?.applicationContext as MeterCollectionApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scannerViewModel = ViewModelProvider(this, viewModelFactory).get(ScannerViewModel::class.java)
        cameraExecutor = Executors.newSingleThreadExecutor()
        startCamera()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.scannerViewModel = scannerViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerViewModel.uiState.observe(viewLifecycleOwner) {
            val newState = it ?: return@observe
            when {
                newState.inProcess -> binding.twStatusResult.text = getText(R.string.scanning_process)
                newState.scanningDone -> {
                    if (!newState.scanError) {
                        // распозналось без ошибок
                        binding.twStatusLabel.text = getText(R.string.device_recognized)
                        binding.twStatusResult.text = newState.objectName
                        binding.btnNext.visibility = View.VISIBLE
                    } else {
                        // в qr коде не соответствующий формат
                        binding.twStatusResult.text = getText(R.string.device_recognized)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get() // извлечь результат из Future

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { result ->
                        Log.d("scanner", result.text)
                        //scannerViewModel.scanningDone(result.text)
                        scannerViewModel.scanningDone("123:0:name")
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e("scanner", "Use case binding failed", exc)
            }
        },
        ContextCompat.getMainExecutor(requireContext()))
    }
}
