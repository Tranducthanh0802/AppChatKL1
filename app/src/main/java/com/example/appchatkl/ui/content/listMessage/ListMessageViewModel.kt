package com.example.appchatkl.ui.content.listMessage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.data.Message
import com.example.appchatkl.data.User
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class ListMessageViewModel : ViewModel() {
    private val _conversation = MutableLiveData<List<Conversation>>()
    val conversation: LiveData<List<Conversation>>
        get() = _conversation
    val TAG="ListMessageViewModel"
    // TODO: Implement the ViewModel


    fun getChat(database: DatabaseReference,list:ArrayList<Conversation>, host: String) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI

                val post = dataSnapshot!!.child("conversation").children
                post.forEach {
                    var name: String=""
                    var linkPhoto:String =""
                    var message=""
                    if (Check(it.key, host)) {
                        if (people(it.key).size > 2) {
                             people(it.key).forEach  {
                                if(!dataSnapshot.child("user").child(it.toString())
                                        .child("fullName").getValue().toString().equals("null"))
                                name += dataSnapshot.child("user").child(it.toString())
                                    .child("fullName").getValue().toString() + ","
                                  }
                            if(!dataSnapshot.child("user").child(it.toString())
                                    .child("linkPhoto").getValue().toString().equals("null"))
                            linkPhoto= dataSnapshot.child("user").child(it.toString())
                                .child("linkPhoto").getValue().toString()
                        }else{
                            people(it.key).forEach {
                                if(!dataSnapshot.child("user").child(it.toString())
                                        .child("fullName").getValue().toString().equals("null")) {
                                    name += dataSnapshot.child("user").child(it.toString())
                                        .child("fullName").getValue().toString()
                                    linkPhoto += dataSnapshot.child("user").child(it.toString())
                                        .child("linkPhoto").getValue().toString()
                                }
                            }

                        }
                       message = dataSnapshot!!.child("conversation").child(it.key.toString()).child("message").getValue().toString()

                    }
                    
                    if(!message.equals(""))
                    list.add(Conversation(Message(TakeMessage(message),time = TakeTime(message),avata = linkPhoto),name))

                }
                _conversation.value=list

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        database.addValueEventListener(postListener)
    }

    private fun Check(key: String?, host: String): Boolean {
        val list = people(key)
        list.forEach {
            if (it.equals(host)) return true
        }
        return false
    }

    private fun people(key: String?): List<String> {
        return key.toString().split(",").toList()
    }
    private fun TakeTime(s: String): String {
        if(s=="")return ""
        val message1 = s.split("@@@@@").toTypedArray()
        val message = message1[message1.size-2].split("@@@@").toTypedArray()
        return message[1]
    }
    private fun TakeMessage(s: String): String {
        if(s=="")return ""
        val message2 = s.split("@@@@@").toTypedArray()
        val message1 = message2[message2.size-2].split("@@@@").toTypedArray()[0]
        val message = message1.split("@@@").toTypedArray()[0]
        return message
    }
}