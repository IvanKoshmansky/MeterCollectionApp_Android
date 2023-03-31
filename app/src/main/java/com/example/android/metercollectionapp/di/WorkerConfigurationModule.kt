package com.example.android.metercollectionapp.di

import androidx.work.Configuration
import com.example.android.metercollectionapp.infrastructure.WorkerFactoriesFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WorkerConfigurationModule {

    @Singleton
    @Provides
    fun provideWorkerConfiguration(workerFactoriesFactory: WorkerFactoriesFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactoriesFactory)
            .build()
    }

}

// если в модуле используется аннотация @Provides то класс не должен быть абстрактным (в отличие от случая @Binds)
