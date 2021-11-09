package com.example.appchatkl.ui.content.friend.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class FriendViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val TAG="FriendViewModel"
    private val _response = MutableLiveData<List<User>>()
    val responseTvShow: LiveData<List<User>>
        get() = _response

    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<User>,
        host:String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val Friend= dataSnapshot!!.child("fiend").child(host).child("allId").getValue()

                analyst(Friend.toString()).forEach {
                    if (!it.equals(host) && !dataSnapshot!!.child("user").child(it.toString())
                            .child("id").value.toString().equals("null")) {
                        list.add(
                            User(
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("id").value.toString(),
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("fullName").value.toString(),
                                dataSnapshot!!.child("user").child(it.toString())
                                    .child("linkPhoto").value.toString()
                            )
                        )
                    }
                }

                _response.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        postReference.addValueEventListener(postListener)

    }
        fun analyst(s:String):List<String>{
            return s.split(",").toList()
        }
}