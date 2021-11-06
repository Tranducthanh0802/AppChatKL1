package com.example.appchatkl

import android.app.Application
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AppRepository(val _aplication: Application) {
    val aplication:Application
    val firebase:FirebaseAuth
    var userlivedata:MutableLiveData<FirebaseUser>
    init {
        aplication=_aplication
        firebase= FirebaseAuth.getInstance()
        userlivedata= MutableLiveData()
    }
    fun register(email:String,password: String){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebase.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(aplication.mainExecutor) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        userlivedata.postValue(firebase.currentUser)
                    } else {
                        // If sign in fails, display a message to the user.

                    }
                }
        }

    }
    fun login(email:String,password: String){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebase.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(aplication.mainExecutor) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                    } else {
                        // If sign in fails, display a message to the user.

                    }
                }
        }
    }
    fun getUserlivedata(): LiveData<FirebaseUser> {
        return userlivedata
    }


}