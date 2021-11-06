package com.example.appchatkl.ui.content.createConversation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class CreateConversationViewModel : ViewModel() {
    var TAG = "CreateConversationViewModel"

    // TODO: Implement the ViewModel
    private val _response = MutableLiveData<List<CreateConversation>>()
    val responseTvShow: LiveData<List<CreateConversation>>
        get() = _response
    private val _select = MutableLiveData<List<CreateConversation>>()
    val selectUser: LiveData<List<CreateConversation>>
        get() = _select


    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<CreateConversation>
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val post = dataSnapshot!!.child("user").children
                post.forEach {
                    val children = dataSnapshot!!.child("user").child(it.key.toString()).children

                    list.add(
                        CreateConversation(
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("id").value.toString(),
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("fullName").value.toString(),
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("linkphoto").value.toString()
                        )
                    )

                }
                _response.value = list
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        postReference.addValueEventListener(postListener)

    }

    fun getListSelect(list: ArrayList<CreateConversation>) {
        _select.postValue(list)
    }

    fun saveIF(database: DatabaseReference, id: String, message: Message) {
        database.child("conversation").child(id).setValue(message)
    }

    fun check(database: DatabaseReference, id: String, message: Message)= viewModelScope.launch  {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot!!.child("message").children

                if(dataSnapshot!!.child("message").getValue()==null){
                    saveIF(database,id,message)
                }
                post.forEach {
                    Log.d(TAG, "onDataChange: "+id)
                  if(dataSnapshot!!.child("user").getValue()!=id)
                      saveIF(database,id,message)

                }


            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        database.addValueEventListener(postListener)
    }


}