package com.example.android.metercollectionapp.infrastructure

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.UserManager

class UploadWorkerFactory (
    private val repository: Repository,
    private val userManager: UserManager
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            UploadWorker::class.java.name -> {
                UploadWorker(appContext, workerParameters, repository, userManager)
            }
            else -> null
        }
    }

}
