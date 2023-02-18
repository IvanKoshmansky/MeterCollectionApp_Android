package com.example.android.metercollectionapp.domain

fun encryptPass(login: String, pass: String): String {
    return (login.hashCode() + pass.hashCode()).toString()
}
