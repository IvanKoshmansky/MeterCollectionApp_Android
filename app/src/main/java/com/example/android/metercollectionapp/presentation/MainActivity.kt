package com.example.android.metercollectionapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.R
import com.example.android.metercollectionapp.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainActivity : AppCompatActivity() {

    private var bottomSheetLastState: Int = 0
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MeterCollectionApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(binding.navView, navController)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer)
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

        managePermissions()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        // TODO: добавить в BottomSheet в качестве openableLayout или реализовать программный перехват нажатия кнопки
        // Back чтобы закрыть открытый Bottom Sheet
        return NavigationUI.navigateUp(navController, null)
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
