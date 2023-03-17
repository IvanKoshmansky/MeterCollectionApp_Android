package com.example.android.metercollectionapp.di

import androidx.lifecycle.ViewModel
import com.example.android.metercollectionapp.presentation.viewmodels.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeviceParamsListViewModel::class)
    abstract fun bindDeviceParamsListViewModel(deviceParamsListViewModel: DeviceParamsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DeviceParamsSelectViewModel::class)
    abstract fun bindDeviceParamsSelectViewModel(deviceParamsSelectViewModel: DeviceParamsSelectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectObjectViewModel::class)
    abstract fun bindScanObjectViewModel(selectObjectViewModel: SelectObjectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ObjectsListViewModel::class)
    abstract fun bindObjectsListViewModel(objectsListViewModel: ObjectsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddObjectViewModel::class)
    abstract fun bindAddObjectViewModel(addObjectViewModel: AddObjectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddDeviceParamViewModel::class)
    abstract fun bindAddDeviceParamViewModel(addDeviceParamViewModel: AddDeviceParamViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScannerViewModel::class)
    abstract fun bindScannerViewModel(scannerViewModel: ScannerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WriteValuesViewModel::class)
    abstract fun bindWriteValuesViewModel(writeValuesViewModel: WriteValuesViewModel): ViewModel
}
