package com.example.appchatkl.ui.content.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class TablaoutViewModel:ViewModel() {
    private var _countRecive= MutableLiveData<String>("0")
    val countRecive: LiveData<String>
        get() = _countRecive

    fun getInvitationAndRequest(postReference: DatabaseReference,
                                host:String
    ) = viewModelScope.launch {
        val currentInv:List<String> = ArrayList<String>()
        val currentRequest:List<String> = ArrayList<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                val post = dataSnapshot!!.child("request").child(host).child("receiveRequest").getValue()

                _countRecive.value= (people(post.toString()).size-1).toString()

            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        postReference.addValueEventListener(postListener)

    }
    private fun people(key: String?): List<String> {
        return key.toString().split(",").toList()
    }
}