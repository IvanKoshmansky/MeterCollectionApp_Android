package com.example.android.metercollectionapp.infrastructure

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.android.metercollectionapp.domain.Repository

class UploadWorkerFactory (private val repository: Repository) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UploadWorker::class.java.name -> {
                UploadWorker(appContext, workerParameters, repository)
            }
            else -> null
        }
    }

}
