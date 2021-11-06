package com.example.appchatkl.ui.content.createConversation

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
import com.example.appchatkl.data.CreateConversation
import com.example.appchatkl.data.Message
import com.example.appchatkl.databinding.CreateConversationFragmentBinding
import com.example.appchatkl.ui.content.createConversation.adapter.CreateConversationAdapter
import com.example.appchatkl.ui.content.createConversation.adapter.SelectFriendAdapter
import com.example.appchatkl.ui.content.createConversation.adapter.onclick
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class CreateConversationFragment : Fragment(), onclick {
    var TAG = "CreateConversationFragment"

    companion object {
        fun newInstance() = CreateConversationFragment()
    }

    private lateinit var viewModel: CreateConversationViewModel
    private lateinit var createConversationAdapter: CreateConversationAdapter
    private lateinit var selectFriendAdapter: SelectFriendAdapter
    private lateinit var binding: CreateConversationFragmentBinding
    private val list = ArrayList<CreateConversation>()
    private var s: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.create_conversation_fragment, container, false
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CreateConversationViewModel::class.java)
        createConversationAdapter = CreateConversationAdapter(this)
        selectFriendAdapter = SelectFriendAdapter()
        val database: DatabaseReference
        database = Firebase.database.reference
        var list = ArrayList<CreateConversation>()
        viewModel.getAllUser(database, list)
        binding.recyclerview.apply {
            adapter = createConversationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.responseTvShow.observe(viewLifecycleOwner, { listTvShows ->
            createConversationAdapter.listConversation = listTvShows
            binding.recyclerview.adapter?.notifyDataSetChanged()
            Log.d(TAG, "onActivityCreated: " + createConversationAdapter.itemCount)

        })
        binding.recyclerview2.apply {
            adapter = selectFriendAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }
        viewModel.selectUser.observe(viewLifecycleOwner, { listTvShows ->
            selectFriendAdapter.listConversation = listTvShows as ArrayList<CreateConversation>
            binding.recyclerview2.adapter?.notifyDataSetChanged()
        })

        binding.btnOk.setOnClickListener {
            s=""
            selectFriendAdapter.listConversation.forEach {
                s += it.id + ","
            }

            viewModel.check(database, s, Message())
        }

    }

    override fun select(name: CreateConversation) {
        list.add(name)

        viewModel.getListSelect(list)

    }

    override fun minus(conversation: CreateConversation) {
        list.remove(conversation)
        viewModel.getListSelect(list)

    }

}