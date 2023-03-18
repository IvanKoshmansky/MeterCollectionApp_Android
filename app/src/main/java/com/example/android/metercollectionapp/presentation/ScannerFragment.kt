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
import androidx.navigation.fragment.findNavController
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.FragmentScannerBinding
import com.example.android.metercollectionapp.di.ViewModelFactory
import com.example.android.metercollectionapp.domain.QrCodeAnalyzer
import com.example.android.metercollectionapp.presentation.viewmodels.ScannerViewModel
import com.google.android.material.snackbar.Snackbar
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
        if (savedInstanceState == null) {
            val arguments = ScannerFragmentArgs.fromBundle(requireArguments())
            scannerViewModel.setupScannerFeature(arguments.scannerFeature)
        }
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
        scannerViewModel.uiState.observe(viewLifecycleOwner) { newState ->
            when {
                // статус сканирования - в процессе
                newState.inProcess -> binding.twStatus.text = getText(R.string.scanning_process)
                newState.scanSuccess -> {
                    // устройство есть в БД либо это новое устройство
                    binding.twStatusLabel.text = getText(R.string.device_recognized)
                    binding.twDeviceRecognized.text = newState.objectName
                    // активировать кнопку "Далее"
                    binding.btnNext.visibility = View.VISIBLE
                }
                newState.scanFormatError -> {
                    // скан не распознан правильно - ошибка формата
                    binding.twStatus.text = getText(R.string.device_not_recognized)
                }
                newState.scanNotFound -> {
                    // устройство не найдено в локальной БД
                    binding.twStatus.text = getText(R.string.device_not_found_in_device_list)
                    binding.twDeviceRecognized.text = getString(R.string.device_guid_name_placeholders,
                        newState.objectGuid, newState.objectName)  // строковый ресурс с placeholders
                }
                newState.newObjectSaved -> Snackbar.make(binding.root, R.string.new_object_save_success,
                    Snackbar.LENGTH_SHORT).show()
            }
        }

        scannerViewModel.navigationToWriteValuesLiveData.observe(viewLifecycleOwner) {
            if (it) {
                scannerViewModel.uiState.value?.let { uiState ->
                    findNavController().navigate(
                        ScannerFragmentDirections.actionScannerFragmentToWriteValuesFragment(
                            uiState.objectGuid,
                            uiState.objectName
                        )
                    )
                    scannerViewModel.navigationToWriteValuesLiveDataDone()
                }
            }
        }

        scannerViewModel.navigationBackLiveData.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigateUp()
                scannerViewModel.navigationBackDone()
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
                        // остановить генерацию кадров
                        preview.setSurfaceProvider(null)
                        // передать в очередь событий потока UiThread Runnable с кодом обработки результата сканирования
                        activity?.runOnUiThread {
                            scannerViewModel.scanningDone(result.text)
                            //scannerViewModel.scanningDone("123:0:name")
                        }
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

//навигация с параметрами включая определение popUpTo - на какой фрагмент переходить
//включая одновременно с ним флаг inclusive, т.е. убрать из стека еще и фрагмент на который указывает popUp
//val navOptions = NavOptions.Builder()
//    .setPopUpTo(R.id.fragment_select_object, false)
//    .build()
//findNavController().navigate(
//ScannerFragmentDirections.actionScannerFragmentToWriteValuesFragment(), navOptions
//)
// держите ваши UI контроллеры максимально "худыми" насколько это возможно - из рекомендаций гугл по архитектуре
// но вместе с тем, ViewModel должна как можно меньше "знать" о деталях реализации UI контроллеров
//newState.inProcess -> run {
//    binding.twStatus.text = getText(R.string.scanning_process)
//    return@run  // не перебирать дальше варианты
//}
