package com.example.android.metercollectionapp.data.remote

import android.util.Log
import com.example.android.metercollectionapp.SyncStatus
import com.example.android.metercollectionapp.data.remote.entities.RemoteUser
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
private const val COLLECTION_USERINFO_EMAIL_FIELD = "email"
private const val COLLECTION_USERINFO_FULLNAME_FIELD = "fullname"

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
                        .whereEqualTo(COLLECTION_USERINFO_EMAIL_FIELD, email)
                        .get()
                        .addOnSuccessListener { documents ->
                            var fullName: String? = null
                            for (document in documents) {
                                if (document.data.containsKey(COLLECTION_USERINFO_FULLNAME_FIELD)) {
                                    fullName = document.data[COLLECTION_USERINFO_FULLNAME_FIELD] as? String
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

}
