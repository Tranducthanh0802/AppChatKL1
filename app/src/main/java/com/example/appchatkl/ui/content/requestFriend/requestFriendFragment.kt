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
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.RequestFriendFragmentBinding
import com.example.appchatkl.ui.content.friend.friend.adapter.FriendAdapter
import com.example.appchatkl.ui.content.requestFriend.adpater.InvitationAdapter
import com.example.appchatkl.ui.content.requestFriend.adpater.RequestAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class requestFriendFragment : Fragment() {
    lateinit var binding: RequestFriendFragmentBinding
    private  val id="oy0QzUqO49QPdWZsXlraOvEMvHc2"
    private val TAG="requestFriendFragment"
    companion object {
        fun newInstance() = requestFriendFragment()
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
        val database: DatabaseReference
        database = Firebase.database.reference
        var list = ArrayList<User>()
        var list1= ArrayList<User>()

        viewModel.getInvitationAndRequest(database, list,list1,id)
        val invitationAdapter= InvitationAdapter()
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
        val requestAdapter= RequestAdapter()
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

}