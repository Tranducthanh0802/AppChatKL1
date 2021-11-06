package com.example.appchatkl.ui.content.allFriend

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.data.User
import com.example.appchatkl.databinding.AllFriendFragmentBinding
import com.example.appchatkl.ui.content.friend.friend.adapter.FriendAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AllFriendFragment : Fragment() {
    lateinit var binding: AllFriendFragmentBinding
    companion object {
        fun newInstance() = AllFriendFragment()
    }

    private lateinit var viewModel: AllFriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(
            inflater,
            R.layout.all_friend_fragment, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllFriendViewModel::class.java)
        val database: DatabaseReference
        database = Firebase.database.reference
        var list = ArrayList<User>()
        viewModel.getAllUser(database, list)
        val friendAdapter= FriendAdapter()
        binding.stickyListFriend.apply {
            adapter = friendAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.responseTvShow.observe(viewLifecycleOwner  ,{
            friendAdapter.listConversation = it
            binding.stickyListFriend.adapter?.notifyDataSetChanged()
        })
    }

}