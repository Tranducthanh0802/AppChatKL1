package com.example.appchatkl.ui.content.requestFriend

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

class RequestFriendViewModel : ViewModel() {
    val TAG="RequestFriendViewModel"
    private var _invitation=MutableLiveData<List<User>>()
    val invitation:LiveData<List<User>>
    get() = _invitation
    private var _request=MutableLiveData<List<User>>()
    val request:LiveData<List<User>>
    get() = _request

    fun getInvitationAndRequest(postReference: DatabaseReference,
                      listInvitation: ArrayList<User>,listRequest: ArrayList<User>,host:String
    ) = viewModelScope.launch {
        val currentInv:List<String> = ArrayList<String>()
        val currentRequest:List<String> = ArrayList<String>()

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                listInvitation.clear()
                listRequest.clear()
                val post = dataSnapshot!!.child("request").children
                post.forEach {
                    if(it.key.toString().equals(host)){
                        val invitation = dataSnapshot!!.child("request").child(host).child("receiveRequest").getValue()
                        val request = dataSnapshot!!.child("request").child(host).child("sendRequest").getValue()
                        val currentInv= analysis(invitation as String)
                        val currentRequest= analysis(request as String)

                        currentInv.forEach {
                            listInvitation.add(
                                User(
                                    dataSnapshot!!.child("user").child(it.toString())
                                        .child("id").value.toString(),
                                    dataSnapshot!!.child("user").child(it.toString())
                                        .child("fullName").value.toString(),
                                    dataSnapshot!!.child("user").child(it.toString())
                                        .child("linkPhoto").value.toString()
                                )
                            )
                            Log.d(TAG, "onDataChange: "+listInvitation.get(0).fullName)
                        }
                        currentRequest.forEach {
                            listRequest.add(
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


                }
                _invitation.value = listInvitation
                _request.value=listRequest
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        postReference.addValueEventListener(postListener)

    }
    private fun analysis(post: String): List<String> {
        val current = post.split(",").toList()

        return current
    }
}