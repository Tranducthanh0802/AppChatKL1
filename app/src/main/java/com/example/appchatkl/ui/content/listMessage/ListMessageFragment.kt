package com.example.appchatkl.ui.content.listMessage

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.data.Conversation
import com.example.appchatkl.databinding.ListMessageFragmentBinding
import com.example.appchatkl.ui.content.chat.ChatFragment
import com.example.appchatkl.ui.content.listMessage.adapter.ConversationAdapter
import com.example.appchatkl.ui.content.listMessage.adapter.SendData
import com.example.appchatkl.ui.content.listMessage.adapter.onClick
import com.example.appchatkl.ui.content.user.BottomViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.net.ssl.ManagerFactoryParameters
import kotlin.math.sign

class ListMessageFragment : Fragment(),onClick {
    var id =""
    var TAG="ListMessageFragment"

     var conversationAdapter:ConversationAdapter=ConversationAdapter(this)
     private lateinit var  binding :ListMessageFragmentBinding
    private lateinit var sendData: SendData
    private lateinit var viewModel: ListMessageViewModel
    val viewModel1:BottomViewModel by activityViewModels()

    companion object {
        fun newInstance() = ListMessageFragment()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater,
            R.layout.list_message_fragment, container, false)
        sendData=activity as SendData
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListMessageViewModel::class.java)
      //  val viewModel1=ViewModelProvider(this).get(BottomViewModel::class.java)
        var auth: FirebaseAuth = Firebase.auth
        val currentUser: FirebaseUser? = auth.currentUser
        id=commomFunction.getId(currentUser!!).toString()
        val database: DatabaseReference
        database = Firebase.database.reference
        val list=ArrayList<Conversation>()
        binding.recyclListChat.apply {
            adapter = conversationAdapter
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL,
                false
            )
            setHasFixedSize(true) }
        viewModel.getChat(database,list,id)
        viewModel.conversation.observe(viewLifecycleOwner,{
            conversationAdapter.listConversation=it

            conversationAdapter.filter.filter("a")
            conversationAdapter.notifyDataSetChanged()
        })
        viewModel1.edt.observe(viewLifecycleOwner,{
            conversationAdapter.get()
           conversationAdapter.filter.filter(it)
        })
        // TODO: Use the ViewModel
    }

    override fun openChat(s:String) {

        sendData.send(s)

    }



}