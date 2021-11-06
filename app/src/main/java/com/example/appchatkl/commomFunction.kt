package com.example.appchatkl

import com.google.firebase.auth.FirebaseUser

object commomFunction {
    fun getId(user:FirebaseUser):String{
       return user.uid
    }
}