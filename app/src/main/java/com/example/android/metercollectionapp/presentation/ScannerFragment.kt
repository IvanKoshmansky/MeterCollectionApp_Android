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
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun startCamera() {
        // ProcessCameraProvider позволяет привязать жизненный цикл камеры к жизненному циклу другого компонента
        // ListenableFuture - это Future (объект которых хранит результат выполнения асинхронной задачи),
        // который может иметь listener'ы - callback'и которые выполняются когда задача выполнилсь
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // 1. Runnable, который запуститься когда в закончится процесс извлечения CameraProvider из переданного контекста
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get() // извлечь результат из Future

            // Preview
            // Так называемый UseCase для работы камеры в режиме Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    // назначить в качестве SurfaceProvider для объекта Preview визуальный компонент
                    // androidx.camera.view.PreviewView
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }

            // use case для ImageAnalyser
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, QrCodeAnalyzer { result ->
                        Log.d("scanner", result.text)
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                // ключевой момент: привязать BACK камеру к жизненному циклу Activity
                // камера работает в use case Preview, ImageCapture, ImageAnalyser и Recorder
                cameraProvider.bindToLifecycle(viewLifecycleOwner, cameraSelector, preview, imageAnalyzer)
            } catch(exc: Exception) {
                Log.e("scanner", "Use case binding failed", exc)
            }
        },
        // 2. Executor, который будет управлять процессом выполнения Runnable (первый параметр)
        // передается Executor который связан с потоком MainThread
        ContextCompat.getMainExecutor(requireContext()))
    }
}
