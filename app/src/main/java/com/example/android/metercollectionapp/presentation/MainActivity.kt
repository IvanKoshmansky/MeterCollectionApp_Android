package com.example.android.metercollectionapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android.metercollectionapp.*
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FragmentContainerView>

    private var bottomSheetLastState: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MeterCollectionApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)
        bottomSheetLastState = bottomSheetBehavior.state
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if ((newState == BottomSheetBehavior.STATE_DRAGGING) &&
                        (bottomSheetLastState == BottomSheetBehavior.STATE_COLLAPSED)) {
                        binding.bottomSheetContainer.getFragment<BottomSheetFragment>().onSlideBegin()
                    }
                    bottomSheetLastState = newState
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        // This line is only necessary if using the default action bar.
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)
        NavigationUI.setupWithNavController(binding.toolbar, navController) // используем Toolbar совместно с navController

        // перехват нажатий на иконки Toolbar
        binding.toolbar.setNavigationOnClickListener {
            val currentDestination = navController.currentDestination ?: return@setNavigationOnClickListener
            if (currentDestination.id == R.id.selectObjectFragment) {
                // ограничение: через childFragmentManager можно обратиться только к стеку фрагментов
                // найти конкретный фрагмент по id нельзя (возвращает null)
                // либо второй вариант - привязать к графу навигации ViewModel и обрабатывать состояния ViewModel
                val fragment = navHostFragment.childFragmentManager.fragments[0] ?: return@setNavigationOnClickListener
                (fragment as? BeforeNavigateUpContract)?.beforeNavigateUp()
            }
            CollapseBottomSheet()
            NavigationUI.navigateUp(navController, null)  // стандартное поведение
        }

        // перехват нажатий на СИСТЕММНУЮ кнопку Back
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                CollapseBottomSheet()
                NavigationUI.navigateUp(navController, null)
            }
        })

        managePermissions()
    }

    // для стандартной обработки нажатий кнопки Back в Action Bar
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.navHostFragment)
//        return NavigationUI.navigateUp(navController, null)
//    }

    private fun CollapseBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetLastState = bottomSheetBehavior.state
    }

    private fun managePermissions() {
        // создать объект для запроса разрешений
        val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                (applicationContext as MeterCollectionApplication).setCameraPermissionGranted()
            }
        }
        // проверить возможно разрешение было дано в предыдущий запуск приложения
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
            PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.CAMERA)
        } else {
            (applicationContext as MeterCollectionApplication).setCameraPermissionGranted()
        }
    }

}
