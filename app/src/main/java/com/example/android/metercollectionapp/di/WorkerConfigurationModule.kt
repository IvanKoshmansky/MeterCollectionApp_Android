package com.example.android.metercollectionapp.di

import androidx.work.Configuration
import com.example.android.metercollectionapp.infrastructure.UploadWorkerFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class WorkerConfigurationModule {

    @Singleton
    @Provides
    fun provideWorkerConfiguration(uploadWorkerFactory: UploadWorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(uploadWorkerFactory)
            .build()
    }

}

// если в модуле используется аннотация @Provides то класс не должен быть абстрактным (в отличие от случая @Binds)
