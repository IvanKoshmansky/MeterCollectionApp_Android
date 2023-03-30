package com.example.android.metercollectionapp.infrastructure

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.android.metercollectionapp.domain.Repository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadWorkerFactory @Inject constructor (private val repository: Repository) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return UploadWorker(appContext, workerParameters, repository)
    }

}
