package com.example.android.metercollectionapp.data.remote

import android.util.Log
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDevice
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteDeviceParam
import com.example.android.metercollectionapp.data.remote.datatransferobjects.RemoteUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val EMAIL = "@gmail.com"

private const val COLLECTION_USERINFO = "userinfo"
private const val COLLECTION_USERINFO_FIELD_EMAIL = "email"
private const val COLLECTION_USERINFO_FIELD_FULLNAME = "fullname"

private const val COLLECTION_DEVICES = "devices"
private const val COLLECTION_DEVICES_FIELD_DEV_TYPE = "dev_type"
private const val COLLECTION_DEVICES_FIELD_GUID = "guid"
private const val COLLECTION_DEVICES_FIELD_LAT_LON = "lat_lon"
private const val COLLECTION_DEVICES_FIELD_NAME = "name"

class FirebaseRemoteDataSource : RemoteDataSource {

    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    private fun getUserEmailByLogin(userLogin: String) = userLogin + EMAIL

    override suspend fun authUser(remoteUser: RemoteUser): RemoteUser? {
        val email = getUserEmailByLogin(remoteUser.login)
        val pass = remoteUser.passEncrypted  // пароли в Room храняться в зашифрованном виде
        var result: RemoteUser? = null

        if (firebaseAuth == null) firebaseAuth = Firebase.auth
        firebaseAuth?.let { auth ->

            firebaseUser = suspendCoroutine { continuation ->

                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            Log.d("firebase", "signInWithEmailAndPassword success")
                            continuation.resume(auth.currentUser)
                        } else {
                            Log.d("firebase", "signInWithEmailAndPassword failed")
                            continuation.resume(null)
                        }
                    }
            }

            if (firebaseUser != null) {
                // аутентификация прошла успешно
                // получить структуру с подробной информацией о пользователях
                val fullName: String? = suspendCoroutine { continuation ->

                    db.collection(COLLECTION_USERINFO)
                        .whereEqualTo(COLLECTION_USERINFO_FIELD_EMAIL, email)
                        .get()
                        .addOnSuccessListener { documents ->
                            var fullName: String? = null
                            for (document in documents) {
                                if (document.data.containsKey(COLLECTION_USERINFO_FIELD_FULLNAME)) {
                                    fullName = document.data[COLLECTION_USERINFO_FIELD_FULLNAME] as? String
                                }
                                break  // только первый элемент
                            }
                            continuation.resume(fullName)
                        }
                        .addOnFailureListener { exception ->
                            exception.printStackTrace()
                            continuation.resume(null)
                        }
                }

                if (fullName != null) {
                    // подробная информация о пользователе прочитана успешно
                    result = remoteUser.copy(
                        status = SyncStatus.SUCCESS,
                        name = fullName
                    )
                }
            }
        }

        return result
    }

    override suspend fun signOut() {
        firebaseAuth?.signOut()
    }

    override suspend fun syncDevices(remoteDevices: List<RemoteDevice>): List<RemoteDevice> {
        // id устройств которые есть на планшете
        val ids = remoteDevices.map { it.guid }
        if (ids.isNotEmpty()) {
            // есть что синхронизовать
            // serverIds - Guid'ы устройств зарегистрированных на сервере
            val serverIds = suspendCoroutine<List<Long>> { continuation ->

                db.collection(COLLECTION_DEVICES)
                    .whereIn(COLLECTION_DEVICES_FIELD_GUID, ids)
                    .get()
                    .addOnSuccessListener { documents ->
                        val ids = mutableListOf<Long>()
                        for (document in documents) {
                            // проход по содержимому документа
                            if (document.data.containsKey(COLLECTION_DEVICES_FIELD_GUID)) {
                                val id = document.data[COLLECTION_DEVICES_FIELD_GUID] as? Long
                                if (id != null) {
                                    ids.add(id)
                                }
                            }
                        }
                        continuation.resume(ids)
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                        continuation.resume(listOf())  // ошибка - возвращаем пустой список
                    }
            }

            // множество подтвержденных GUID которые зарегистированы на сервере
            val setOfServerIds = serverIds.toSet()
            return remoteDevices.map {
                RemoteDevice(
                    guid = it.guid,
                    devType = it.devType,
                    name = it.name,
                    lat = it.lat,
                    lon = it.lon,
                    // успешно или ошибка (такой GUID не существует на сервере)
                    status = if (it.guid in setOfServerIds) { SyncStatus.SUCCESS } else { SyncStatus.FAILED }
                )
            }
        } else {
            return remoteDevices  // пустой список
        }
    }

    override suspend fun syncDeviceParams(remoteDeviceParams: List<RemoteDeviceParam>): List<RemoteDeviceParam> {
        TODO("Not yet implemented")
    }

}
