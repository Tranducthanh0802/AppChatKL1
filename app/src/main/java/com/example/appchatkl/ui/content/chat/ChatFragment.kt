package com.example.appchatkl.ui.content.chat

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Message
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.databinding.ChatFragmentBinding
import com.example.appchatkl.ui.content.chat.adapter.ChatAdapter
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatFragment : Fragment() {
    val TAG="ChatFragment"
    private  val id="3CHe0xpw0POKGRem9T3UmMTBkih2"
    private lateinit var binding:ChatFragmentBinding
    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding = DataBindingUtil.inflate(
            inflater,
            R.layout.chat_fragment, container, false
        )
        return binding.root
    }
    var list = ArrayList<com.example.appchatkl.data.Message>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        val databse =Firebase.database.reference
        val adapterBinding=ChatAdapter()
        binding.recyclerviewmessage.apply {
            adapter = adapterBinding
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
        }
        adapterBinding.listConversation
        viewModel.takeMessage(id,databse,list,id)
        viewModel.list.observe(viewLifecycleOwner,{
            adapterBinding.listConversation=it
            adapterBinding.notifyDataSetChanged()
        })

    }

}