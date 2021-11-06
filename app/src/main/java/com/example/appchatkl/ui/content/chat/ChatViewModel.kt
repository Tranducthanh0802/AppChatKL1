package com.example.appchatkl.ui.content.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import kotlin.math.log

class ChatViewModel : ViewModel() {
    private val Conversation = "conversation"
    private val Message = "message"
    private val TAG = "ChatViewModel"

    private var _list = MutableLiveData<List<Message>>()
    val list: LiveData<List<Message>>
        get() = _list

    // TODO: Implement the ViewModel
    fun takeMessage(
        id: String,
        postReference: DatabaseReference,
        listMS: ArrayList<Message>
    ,host: String
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listMS.clear()
                // Get Post object and use the values to update the UI
                val post = dataSnapshot!!.child(Conversation).children
                //val post = dataSnapshot!!.child(Conversation).child(Message).getValue().toString()

                post.forEach {
                    if (it.key.toString().equals(id + ",")) {
                        val post = dataSnapshot!!.child(Conversation).child(it.key.toString())
                            .child(Message).getValue().toString()
                        CreateGroup(post, listMS,host)

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message

            }
        }
        postReference.addValueEventListener(postListener)

    }
    private fun LeftOrRight(id:String,host:String):Boolean{
        if(id.equals(host)){
            return true
        }
        return false
    }

    private fun CreateGroup(post: String, listMS: ArrayList<Message>,host:String) {
        val listMessage = post.split("@@@@@").toList()
        var time: String
        var id: String
        var message: String
        listMessage.forEach {
            time = TakeTime(it)
            id = TakeID(it)
            message = TakeMessage(it)
            listMS.add(Message(message, id, time))
        }
        var k=0
        listMS.forEach{
            if(it.id==host && k==0){
                it.isShowAvata=true
                k=1
            }else{
                k=0
            }

        }
        for (i in 0..listMS.size) {
            Log.d(TAG, "CreateGroup: "+i+" "+listMS.size)
            if(i+1==listMS.size || listMS.get(i).id!=listMS.get(i+1).id){
                listMS.get(i).isShowTime=true
                if(i+1==listMS.size) break
            }
        }
        _list.value = listMS
    }

    private fun TakeMessage(s: String): String {
        if(s=="")return ""
        val message1 = s.split("@@@@").toTypedArray()[0]
        val message = message1.split("@@@").toTypedArray()[0]
        return message
    }

    private fun TakeTime(s: String): String {
        if(s=="")return ""
        val message = s.split("@@@@").toTypedArray()
        Log.d(TAG, "TakeTime: "+message[1])
        return message[1]
    }

    private fun TakeID(s: String): String {
        if(s=="")return ""
        val message1 = s.split("@@@@").toTypedArray()[0]
        val message = message1.split("@@@").toTypedArray()[1]

        return message
    }
    private fun TakeImage(s: String): String {
        val message = s.split("@@").toTypedArray()[0]
        return message
    }

    private fun analysis(post: String, id: String): Boolean {
        val list = post.split(",").toTypedArray()
        list.forEach {

            if (it == id) return true
            else return false
        }
        return false
    }
}