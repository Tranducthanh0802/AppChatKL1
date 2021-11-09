package com.example.appchatkl.ui.content.requestFriend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.RequestFriendFragmentBinding
import com.example.appchatkl.ui.content.requestFriend.adpater.Decision
import com.example.appchatkl.ui.content.requestFriend.adpater.InvitationAdapter
import com.example.appchatkl.ui.content.requestFriend.adpater.RequestAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RequestFriendFragment : Fragment(),Decision {
    lateinit var binding: RequestFriendFragmentBinding
    private  var id=""
    private val TAG="requestFriendFragment"
    lateinit var database: DatabaseReference
    companion object {
        fun newInstance() = RequestFriendFragment()
    }

    private lateinit var viewModel: RequestFriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(
            inflater,
            R.layout.request_friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RequestFriendViewModel::class.java)

        database = Firebase.database.reference
        var list = ArrayList<User>()
        var list1= ArrayList<User>()
        var auth: FirebaseAuth = Firebase.auth
        val currentUser: FirebaseUser? = auth.currentUser
        id= commomFunction.getId(currentUser!!).toString()
        viewModel.getInvitationAndRequest(database, list,list1,id)
        val invitationAdapter= InvitationAdapter(this)
        binding.recInviteReceive.apply {
            adapter = invitationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.invitation.observe(viewLifecycleOwner  ,{
            invitationAdapter.listConversation = it
            Log.d(TAG, "onActivityCreated: "+it+" "+invitationAdapter.listConversation)
            binding.recInviteReceive.adapter?.notifyDataSetChanged()
        })
        val requestAdapter= RequestAdapter(this)
        binding.recInviteSend.apply {
            adapter = requestAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.request.observe(viewLifecycleOwner  ,{
            requestAdapter.listConversation = it
            Log.d(TAG, "onActivityCreated: "+it+" "+invitationAdapter.listConversation)
            binding.recInviteSend.adapter?.notifyDataSetChanged()
        })
    }


    override fun onClickYes(s: String) {
        val a= viewModel.delete(viewModel.idSendReQuest.value.toString(),s)
        database.child("request").child(id).child("receiveRequest").setValue(a)
        val b=viewModel.friend.value+s+","
        database.child("fiend").child(id).child("allId").setValue(b)
    }

    override fun onClikNo(s: String) {
        val a= viewModel.delete(viewModel.idReceiveReQuest.value.toString(),s)
        database.child("request").child(id).child("sendRequest").setValue(a)
    }

}