package com.example.android.metercollectionapp.di

import com.example.android.metercollectionapp.MeterCollectionApplication
import com.example.android.metercollectionapp.data.RepositoryImpl
import com.example.android.metercollectionapp.data.localdb.LocalDatabase
import com.example.android.metercollectionapp.data.localdb.getDatabase
import com.example.android.metercollectionapp.data.storage.Storage
import com.example.android.metercollectionapp.data.storage.getSharedPreferencesStorage
import com.example.android.metercollectionapp.domain.Repository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Singleton
    @Provides
    fun provideLocalDatabase(context: MeterCollectionApplication): LocalDatabase {
        return getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideStorage(context: MeterCollectionApplication): Storage {
        return getSharedPreferencesStorage(context)
    }
    // здесь Storage - интерфейс, зависимость на интерфейс

    @Singleton
    @Provides
    fun provideRepository(localDatabase: LocalDatabase, storage: Storage): Repository {
        return RepositoryImpl(localDatabase, storage)
    }

}
