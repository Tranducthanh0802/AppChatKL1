package com.example.appchatkl.ui.content.allFriend

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

class AllFriendViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _response = MutableLiveData<List<User>>()
    val responseTvShow: LiveData<List<User>>
        get() = _response

    fun getAllUser(
        postReference: DatabaseReference,
        list: ArrayList<User>
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                list.clear()
                val post = dataSnapshot!!.child("user").children
                post.forEach {
                    list.add(
                        User(
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("id").value.toString(),
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("fullName").value.toString(),
                            dataSnapshot!!.child("user").child(it.key.toString())
                                .child("linkPhoto").value.toString()
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
}