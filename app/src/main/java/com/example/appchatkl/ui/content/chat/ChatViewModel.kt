package com.example.appchatkl.ui.content.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appchatkl.data.CreateConversation
import com.example.appchatkl.data.Message
import com.example.appchatkl.data.db.AppDatabase
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
    var avata = MutableLiveData<String>("")
    var name = MutableLiveData<String>("")
    var count = MutableLiveData<Int>(0)
    var message = MutableLiveData<String>()
    var idName = MutableLiveData<String>()
    var max = MutableLiveData<Int>(0)


    // TODO: Implement the ViewModel
    fun takeMessage(
        id: String,
        postReference: DatabaseReference,
        listMS: ArrayList<Message>, host: String,
        chatDB: AppDatabase
    ) = viewModelScope.launch {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listMS.clear()
                // Get Post object and use the values to update the UI
                val post = dataSnapshot!!.child(Conversation).children
                var na = ""
                var link = ""
                var comma = ""
                post.forEach {
                    if (compare(it.key.toString(), id)) {
                        val post =
                            dataSnapshot!!.child(Conversation).child(it.key.toString().trim())
                                .child(Message).getValue().toString()
                        if (!post.equals("")) CreateGroup(post, listMS, host, postReference)
                        idName.value = it.key.toString()
                        people(it.key.toString()).forEach {

                            if (!dataSnapshot.child("user").child(it.toString())
                                    .child("fullName").getValue().toString()
                                    .equals("null") && !it.toString().equals(host)
                            ) {
                                na += comma + dataSnapshot.child("user").child(it.toString())
                                    .child("fullName").getValue().toString()
                                link += dataSnapshot.child("user").child(it.toString())
                                    .child("linkPhoto").getValue().toString()
                                comma = ","
                            }
                        }
                        if (link.equals("null")) link = ""
                        name.value = na
                        avata.value = link
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                chatDB.chatDao().loadConversation().forEach {
                    if (id.equals(it.idTeam)) {
                        name.value = it.nameNhom
                        avata.value = it.linkPhotoTeam
                        CreateGroupOff(it.idTeam, listMS, host, chatDB)
                    }

                }

            }
        }
        postReference.addValueEventListener(postListener)

    }

    fun takeMessageOff(
        id: String,
        listMS: ArrayList<Message>, host: String,
        chatDB: AppDatabase
    ) = viewModelScope.launch {
        // Get Post object and use the values to update the UI

        var na = ""
        var link = ""
        var comma = ""

        chatDB.chatDao().loadConversation().forEach {
            if (id.equals(it.idTeam)) {
                name.value = it.nameNhom
                avata.value = it.linkPhotoTeam
                CreateGroupOff(it.mesage, listMS, host, chatDB)
            }

        }
    }

    fun Send(
        s: String,
        postReference: DatabaseReference,
        id: String,
        host: String,
        chatDB: AppDatabase
    ) {
        var k = 0
        Log.d(TAG, "Send: " + host)
        postReference.child(Conversation).child(id).child("id").setValue(host)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (k == 0) {
                    val post =
                        dataSnapshot!!.child(Conversation).child(id).child(Message).getValue()
                    message.value = post.toString() + s
                    k = 1
                    if (dataSnapshot!!.child(Conversation).child(id).child("id").getValue()
                            .toString().equals(host)
                    ) {
                        count.value =
                            dataSnapshot!!.child(Conversation).child(id).child("count").getValue()
                                .toString().toInt()
                        count.value = count.value!! + 1
                    } else {
                        count.value = count.value!! + 1
                    }
                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                chatDB.chatDao().loadConversation().forEach {
                    if (id.equals(it.idTeam)) {
                        message.value = it.mesage
                        count.value = it.count.toInt()
                    }
                }
            }
        }
        postReference.addValueEventListener(postListener)

    }

    private fun LeftOrRight(id: String, host: String): Boolean {
        if (id.equals(host)) {
            return true
        }

        return false
    }

    private fun CreateGroup(
        post: String,
        listMS: ArrayList<Message>,
        host: String,
        postReference: DatabaseReference
    ) {
        val listMessage = post.split("@@@@@").toList()
        if (listMessage.isNotEmpty()) {
            max.value = listMessage.size
        }
        Log.d(TAG, "CreateGroup: ádfdsafsaf")
        var time: String
        var id: String
        var message: String
        var avata: String = ""
        listMessage.forEach {
            var right: Boolean = false
            time = TakeTime(it)
            id = TakeID(it)
            if (LeftOrRight(id, host)) right = true
            message = TakeMessage(it)
            if (!message.equals("")) {
                listMS.add(Message(message, id, time, isRight = right, avata = avata))
            }
        }
        var k = 0
        listMS.forEach {
            if (it.id != host && k == 0) {
                it.isShowAvata = true
                k = 1
            } else {
                k = 0
            }

        }
        for (i in 0..listMS.size) {
            if (i + 1 == listMS.size || listMS.get(i).id != listMS.get(i + 1).id) {
                listMS.get(i).isShowTime = true
                if (i + 1 == listMS.size) break
            }
        }
        for (i in 0..listMS.size - 1) {
            postReference.child("user").child(listMS.get(i).id).child("linkPhoto").get()
                .addOnSuccessListener {
                    listMS.get(i).avata = it.value.toString()
                    _list.value = listMS
                }.addOnFailureListener {

                    Log.e("firebase", "Error getting data", it)
                }
        }
//            Log.d(TAG, "CreateGroup65: "+listMS.get(0).avata)
//            _list.value = listMS
    }

    private fun CreateGroupOff(
        post: String,
        listMS: ArrayList<Message>,
        host: String,
        chatDB: AppDatabase
    ) {
        val listMessage = post.split("@@@@@").toList()
        if (listMessage.isNotEmpty()) {
            max.value = listMessage.size
        }
        Log.d(TAG, "CreateGroup: ádfdsafsaf" + listMessage)
        var time: String
        var id: String
        var message: String
        var avata: String = ""
        listMessage.forEach {
            var right: Boolean = false
            time = TakeTime(it)
            id = TakeID(it)
            if (LeftOrRight(id, host)) right = true
            message = TakeMessage(it)
            if (!message.equals("")) {
                listMS.add(Message(message, id, time, isRight = right, avata = avata))
            }
        }
        var k = 0
        listMS.forEach {
            if (it.id != host && k == 0) {
                it.isShowAvata = true
                k = 1
            } else {
                k = 0
            }

        }
        for (i in 0..listMS.size) {
            if (i + 1 == listMS.size || listMS.get(i).id != listMS.get(i + 1).id) {
                listMS.get(i).isShowTime = true
                if (i + 1 == listMS.size) break
            }
        }
        for (i in 0..listMS.size - 1) {
            chatDB.chatDao().loadUser().forEach {
                if (listMS.get(i).equals(it.id)) {
                    listMS.get(i).avata = it.linkPhoto
                }
            }
            _list.value = listMS

        }
//            Log.d(TAG, "CreateGroup65: "+listMS.get(0).avata)
//            _list.value = listMS
    }

    private fun TakeMessage(s: String): String {
        if (s == "") return ""
        val message1 = s.split("@@@@").toTypedArray()[0]
        val message = message1.split("@@@").toTypedArray()[0]
        return message
    }

    private fun TakeTime(s: String): String {
        if (s == "") return ""
        val message = s.split("@@@@").toTypedArray()
        return message[1]
    }

    private fun TakeID(s: String): String {
        if (s == "") return ""
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

    private fun people(key: String?): List<String> {
        return key.toString().split(",").toList()
    }

    private fun compare(id: String, host: String): Boolean {
        var a: List<String> = id.split(",").toList()
        var b = host.split(",").toList()

        a = a.sortedBy { it.toString().trim() }
        b = b.sortedBy { it.toString().trim() }

        if (a.toString().equals(b.toString())) return true
        return false
    }


}