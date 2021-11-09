package com.example.appchatkl

import com.google.firebase.auth.FirebaseUser

object commomFunction {
    fun getId( a:FirebaseUser):String{
       return a.uid
    }
     fun compare(id:String, host:String):Boolean{
        var a:List<String> =id.split(",").toList()
        var b=host.split(",").toList()

        a=a.sortedBy { it.toString().trim() }
        b=b.sortedBy { it.toString().trim() }

        if( a.toString().equals(b.toString())) return true
        return false
    }
}