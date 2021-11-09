package com.example.appchatkl.ui.content.friend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.appchatkl.R
import com.example.appchatkl.commomFunction
import com.example.appchatkl.databinding.TablayoutFriendFragmentBinding
import com.example.appchatkl.ui.content.friend.adapterTablayout.TablayoutAdapter
import com.example.appchatkl.ui.content.user.BottomViewModel
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class TablayouFiendFragment : Fragment() {
    lateinit var viewModel:TablaoutViewModel
    val TAG="TablayouFiendFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding:TablayoutFriendFragmentBinding= DataBindingUtil.inflate(
            inflater,
            R.layout.tablayout_friend_fragment, container, false
        )
        viewModel = ViewModelProvider(this).get(TablaoutViewModel::class.java)
        binding.viewpagerTab.adapter = TablayoutAdapter(this)
        TabLayoutMediator(binding.tab, binding.viewpagerTab) { tab, position ->
            when(position) {
                0->  tab.text = "BẠN BÈ"
                1->  tab.text = "TẤT CẢ"
                2->  tab.text = "YÊU CẦU"
            }
        }.attach()
        var auth: FirebaseAuth = Firebase.auth
        val currentUser: FirebaseUser? = auth.currentUser
        val id= commomFunction.getId(currentUser!!).toString()
        val database: DatabaseReference
        database = Firebase.database.reference
        viewModel.getInvitationAndRequest(database,id)
        viewModel.countRecive.observe(viewLifecycleOwner,{
            val count=it.toInt()
            Log.d(TAG, "onCreateView: "+count)
            if(count>0) {
                binding.tab.getTabAt(2)?.getOrCreateBadge()?.setNumber(count);
            }else {
                binding.tab.getTabAt(2 )?.removeBadge();
            }
        })
        return binding.root
    }


}