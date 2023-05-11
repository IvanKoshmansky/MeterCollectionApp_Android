package com.example.android.metercollectionapp.data.remote

import android.util.Log
import com.example.android.metercollectionapp.data.remote.entities.RemoteUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val EMAIL = "@gmail.com"

class FirebaseRemoteDataSource : RemoteDataSource {

    private var firebaseAuth: FirebaseAuth? = null
    private var firebaseUser: FirebaseUser? = null

    // добавить к логину постфикс "@gmail.com"
    private fun getUserEmailByLogin(userLogin: String) = userLogin + EMAIL

    // попытка аутентификации
    override suspend fun authUser(remoteUser: RemoteUser): RemoteUser? {
        var fullUserName = ""  // возвращается при успешной аутентификации, полное имя пользователя находится на сервере
        var result: RemoteUser? = null

        if (firebaseAuth == null) firebaseAuth = Firebase.auth
        firebaseAuth?.let { auth ->
            val email = getUserEmailByLogin(remoteUser.login)
            val pass = remoteUser.passEncrypted  // пароли в Room храняться в зашифрованном виде

            // возвращается либо currentUser либо null если аутентификация не прошла
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

        }

        return result
    }

    override suspend fun signOut() {
        firebaseAuth?.signOut()
    }

}
