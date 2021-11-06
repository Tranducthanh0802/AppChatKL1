package com.example.appchatkl.ui.content.listMessage

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
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ListMessageFragmentBinding
import com.example.appchatkl.ui.content.listMessage.adapter.ConversationAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ListMessageFragment : Fragment() {
    val id ="3CHe0xpw0POKGRem9T3UmMTBkih2"
    lateinit var  binding :ListMessageFragmentBinding
    companion object {
        fun newInstance() = ListMessageFragment()
    }

    private lateinit var viewModel: ListMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater,
            R.layout.list_message_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListMessageViewModel::class.java)
        val database: DatabaseReference
        database = Firebase.database.reference
        val list=ArrayList<Conversation>()
        val conversationAdapter=ConversationAdapter()
        viewModel.getChat(database,list,id)
        binding.recyclListChat.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true) }
        viewModel.conversation.observe(viewLifecycleOwner,{
            conversationAdapter.listConversation=it
            Log.d("TAG", "onActivityCreated: "+it)
            conversationAdapter.notifyDataSetChanged()
        })
        // TODO: Use the ViewModel
    }

}