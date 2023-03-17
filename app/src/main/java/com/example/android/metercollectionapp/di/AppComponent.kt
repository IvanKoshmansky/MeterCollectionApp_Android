package com.example.android.metercollectionapp.di

import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.domain.UserManager
import com.example.android.metercollectionapp.presentation.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ViewModelFactoryModule::class, ViewModelsModule::class, DataModule::class])
@Singleton
interface AppComponent {

    // нужно для проведения зависимости на WorkManager
    fun inject(application: MeterCollectionApplication)

    // MainActivity
    fun inject(mainActivity: MainActivity)
    // ...
    // фрагменты где нужен AppComponent и его зависимости
    // ...
    fun inject(mainFragment: MainFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(selectObjectFragment: SelectObjectFragment)
    fun inject(objectsListFragment: ObjectsListFragment)
    fun inject(addObjectFragment: AddObjectFragment)
    fun inject(deviceParamsListFragment: DeviceParamsListFragment)
    fun inject(addDeviceParamFragment: AddDeviceParamFragment)
    fun inject(deviceParamsSelectFragment: DeviceParamsSelectFragment)
    fun inject(scannerFragment: ScannerFragment)
    fun inject(writeValuesFragment: WriteValuesFragment)

    // singleton, доступен везде, где доступен AppComponent
    fun getUserManager(): UserManager
    // можно использовать дополнительно субкомпоненты, чтобы ограничить область видимости этой ссылки
    // и добавить Scope, для этого компоненты/субкомпоненты и нужны, чтобы группировать ссылки

    // для камеры тоже нужен отдельный класс/субкомпонент, он должен обрабатывать lifecycle-сообщения
    // чтобы вовремя освобождать ресурсы

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: MeterCollectionApplication): Builder
        fun build(): AppComponent
    }
}

//@BindsInstance работает в @Component.Builder или @Component.Factory
