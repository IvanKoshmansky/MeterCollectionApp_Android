package com.example.android.metercollectionapp.infrastructure

import androidx.work.DelegatingWorkerFactory
import com.example.android.metercollectionapp.domain.Repository
import com.example.android.metercollectionapp.domain.UserManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkerFactoriesFactory @Inject constructor (
    repository: Repository,
    userManager: UserManager
) : DelegatingWorkerFactory() {

    init {
        addFactory(UploadWorkerFactory(repository, userManager))
    }
}
