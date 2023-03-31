package com.example.android.metercollectionapp.infrastructure

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.metercollectionapp.domain.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UploadWorker(
    context: Context,
    params: WorkerParameters,
    private val repository: Repository) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            repository.sync()
            Result.success() // TODO: сделать передачу результата - статуса через LiveData (см. codelab)
        }
    }

    companion object {
        const val MINIMUM_LATENCY = 5L
        //const val REPEAT_INTERVAL = 15L
        const val REPEAT_INTERVAL = 1L
        const val UNIQUE_UPLOAD_WORKER = "UNIQUEUPLOADWORKER"
        const val UPLOAD_WORKER_TAG = "UPLOADWORKERTAG"
    }

}

// The main difference between a Worker class and a CoroutineWorker is that the doWork() method in a CoroutineWorker
// is a suspend function and can run asynchronous tasks, while Worker’s doWork() can only execute synchronous talks.
// Another CoroutineWorker feature is that it automatically handles stoppages and cancellation while a Worker class
// needs to implement the onStopped() method to cover these cases.

//Result.success(): The work finished successfully.
//Result.failure(): The work failed.
//Result.retry(): The work failed and should be tried at another time according to its retry policy.
