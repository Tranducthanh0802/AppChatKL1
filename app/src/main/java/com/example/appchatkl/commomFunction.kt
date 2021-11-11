package com.example.appchatkl

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.auth.FirebaseUser

object commomFunction {
    fun getId(a: FirebaseUser): String {
        return a.uid
    }
    fun getEmail(a: FirebaseUser): String {
        return a.email.toString()
    }

    fun compare(id: String, host: String): Boolean {
        var a: List<String> = id.split(",").toList()
        var b = host.split(",").toList()

        a = a.sortedBy { it.toString().trim() }
        b = b.sortedBy { it.toString().trim() }

        if (a.toString().equals(b.toString())) return true
        return false
    }

    fun checkConnect(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netWorkInfo = connectivityManager.activeNetworkInfo
        if (netWorkInfo != null && netWorkInfo.isConnectedOrConnecting) return true
        return false
    }
}